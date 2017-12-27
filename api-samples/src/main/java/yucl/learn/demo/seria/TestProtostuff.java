package yucl.learn.demo.seria;

import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import io.protostuff.*;
import io.protostuff.runtime.RuntimeSchema;
import yucl.learn.demo.dto.Products;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestProtostuff {

    static Schema<Products> schema = RuntimeSchema.getSchema(Products.class);
    private long userTime;



    public static void f4() {
        Products instance = new Products();
        instance.setI1(100);
        Schema<Products> schema = RuntimeSchema.getSchema(Products.class);
        byte[] data = JsonIOUtil.toByteArray(instance, schema, false);
        Products pojo = new Products();
        try {
            //JsonIOUtil.writeTo(System.out,instance,schema,false);
            //System.out.println(new String(data,"UTF-8"));
            JsonIOUtil.mergeFrom(data, pojo, schema, false);
            // System.out.println(pojo.getNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void f3() {
        Products instance = new Products();
        instance.setI1(100);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(instance);
            oos.close();
            try (UTF8JsonGenerator jg = JsonIOUtil.newJsonGenerator(System.out, new byte[1024])) {
                jg.writeObject(instance);
                jg.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void f1() {
        TestProtostuff test = new TestProtostuff();
        List<Products> products = new ArrayList<>();
        products.add(new Products());

        List<byte[]> list = test.serializeProtoStuffProductsList(products);
           // System.out.println(test.userTime);
        List<Products> pd = test.deserializeProtoStuffDataListToProductsList(list);
           // System.out.println(test.userTime);

    }

    public static <T> byte[] toByteArray(T message) {
        Schema<T> schema = RuntimeSchema.getSchema((Class<T>) message.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        byte[] protostuff = null;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(message, schema, buffer);
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    public static <T> T toObject(byte[] bytes, Class<T> cls) {
        Schema<T> schema = RuntimeSchema.getSchema(cls);
        try {
            T obj = cls.newInstance();
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<byte[]> serializeProtoStuffProductsList(List<Products> pList) {
        if (pList == null || pList.size() <= 0) {
            return null;
        }
        long start = System.nanoTime();
        List<byte[]> bytes = new ArrayList<byte[]>();
        Schema<Products> schema = RuntimeSchema.getSchema(Products.class);
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        byte[] protostuff = null;
        for (Products p : pList) {
            try {
                protostuff = ProtostuffIOUtil.toByteArray(p, schema, buffer);
                bytes.add(protostuff);
            } finally {
                buffer.clear();
            }
        }
        long end = System.nanoTime();
        this.userTime = end - start;
        return bytes;
    }

    public List<Products> deserializeProtoStuffDataListToProductsList(
            List<byte[]> bytesList) {
        if (bytesList == null || bytesList.size() <= 0) {
            return null;
        }
        long start = System.nanoTime();
        Schema<Products> schema = RuntimeSchema.getSchema(Products.class);
        List<Products> list = new ArrayList<Products>();
        for (byte[] bs : bytesList) {
            Products product = new Products();
            ProtostuffIOUtil.mergeFrom(bs, product, schema);
            list.add(product);
        }
        long end = System.nanoTime();
        this.userTime = end - start;
        return list;
    }
}
