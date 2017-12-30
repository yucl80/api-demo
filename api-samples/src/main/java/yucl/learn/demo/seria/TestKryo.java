package yucl.learn.demo.seria;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import de.javakaffee.kryoserializers.*;
import de.javakaffee.kryoserializers.cglib.CGLibProxySerializer;
import de.javakaffee.kryoserializers.guava.*;
import de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalDateSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalDateTimeSerializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationHandler;
import java.util.*;

public class TestKryo {
   /* private static final ThreadLocal<Kryo> kryos = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // configure kryo instance, customize settings
        return kryo;
    });*/

    static {

        //  kryo.register(Products.class);
        // kryo.register(User.class);
    }


    static KryoFactory factory = () -> {
        //Kryo kryo = new Kryo();
        Kryo kryo= createKryoInstance();
        // configure kryo instance, customize settings
        regist(kryo);
        return kryo;
    };

    private static Kryo createKryoInstance(){
        final Kryo kryo = new KryoReflectionFactorySupport() {

            @Override
            public Serializer<?> getDefaultSerializer(final Class clazz) {
                if (EnumSet.class.isAssignableFrom(clazz)) {
                    return new EnumSetSerializer();
                }
                if (EnumMap.class.isAssignableFrom(clazz)) {
                    return new EnumMapSerializer();
                }
                if (Collection.class.isAssignableFrom(clazz)) {
                    return new CopyForIterateCollectionSerializer();
                }
                if (Map.class.isAssignableFrom(clazz)) {
                    return new CopyForIterateMapSerializer();
                }
                if (Date.class.isAssignableFrom(clazz)) {
                    return new DateSerializer(clazz);
                }
                // see if the given class is a cglib proxy
                if (CGLibProxySerializer.canSerialize(clazz)) {
                    // return the serializer registered for CGLibProxyMarker.class (see above)
                    return getSerializer(CGLibProxySerializer.CGLibProxyMarker.class);
                }

                final Serializer<List<?>> subListSerializer = SubListSerializers.createFor(clazz);
                if ( subListSerializer != null ) {
                    return subListSerializer;
                }
                // protobuf
                /*if ( com.google.protobuf.GeneratedMessage.class.isAssignableFrom( clazz ) ) {
                    return new ProtobufSerializer();
                }*/
                return super.getDefaultSerializer(clazz);
            }

        };
        return kryo;
    }

    static void regist(Kryo kryo) {
        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        kryo.register(Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
        kryo.register(Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
        kryo.register(Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
        kryo.register(Collections.singletonList("").getClass(), new CollectionsSingletonListSerializer());
        kryo.register(Collections.singleton("").getClass(), new CollectionsSingletonSetSerializer());
        kryo.register(Collections.singletonMap("", "").getClass(), new CollectionsSingletonMapSerializer());
        kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
        kryo.register(InvocationHandler.class, new JdkProxySerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);

// custom serializers for non-jdk libs

// register CGLibProxySerializer, works in combination with the appropriate action in handleUnregisteredClass (see below)
        kryo.register(CGLibProxySerializer.CGLibProxyMarker.class, new CGLibProxySerializer());
// dexx
/*        ListSerializer.registerSerializers(kryo);
        MapSerializer.registerSerializers(kryo);
        SetSerializer.registerSerializers(kryo);*/
        // joda DateTime, LocalDate and LocalDateTime
        try {
            Thread.currentThread().getContextClassLoader().loadClass("org.joda.time.DateTime");
            kryo.register(DateTime.class, new JodaDateTimeSerializer());
            kryo.register(LocalDate.class, new JodaLocalDateSerializer());
            kryo.register(LocalDateTime.class, new JodaLocalDateTimeSerializer());
        } catch (ClassNotFoundException e) {

        }
// protobuf
// kryo.register( SampleProtoA.class, new ProtobufSerializer() ); // or override Kryo.getDefaultSerializer as shown below
// wicket
// kryo.register( MiniMap.class, new MiniMapSerializer() );
// guava ImmutableList, ImmutableSet, ImmutableMap, ImmutableMultimap, ReverseList, UnmodifiableNavigableSet
        try {
            Thread.currentThread().getContextClassLoader().loadClass("com.google.common.collect.Table");
            ImmutableListSerializer.registerSerializers(kryo);
            ImmutableSetSerializer.registerSerializers(kryo);
            ImmutableMapSerializer.registerSerializers(kryo);
            ImmutableMultimapSerializer.registerSerializers(kryo);
            ReverseListSerializer.registerSerializers(kryo);
            UnmodifiableNavigableSetSerializer.registerSerializers(kryo);
// guava ArrayListMultimap, HashMultimap, LinkedHashMultimap, LinkedListMultimap, TreeMultimap
            ArrayListMultimapSerializer.registerSerializers(kryo);
            HashMultimapSerializer.registerSerializers(kryo);
            LinkedHashMultimapSerializer.registerSerializers(kryo);
            LinkedListMultimapSerializer.registerSerializers(kryo);
            TreeMultimapSerializer.registerSerializers(kryo);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
    }

    static KryoPool pool = new KryoPool.Builder(factory).softReferences().build();

    public static <T> byte[] toByteArray(T message) {
        Kryo kryo = pool.borrow();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Output output = new Output(baos);
            kryo.writeObject(output, message);
            output.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.release(kryo);
        }
        return new byte[0];
    }

    public static <T> T toObject(byte[] bytes, Class<T> cls) {
        Kryo kryo = pool.borrow();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            Input input = new Input(bais);
            return (T) kryo.readObject(input, cls);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.release(kryo);
        }
        return null;
    }
}
