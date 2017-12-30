package yucl.learn.demo.seria;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
        Kryo kryo = new Kryo();
        // configure kryo instance, customize settings
        return kryo;
    };

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
