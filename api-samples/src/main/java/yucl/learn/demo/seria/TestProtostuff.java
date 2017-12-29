package yucl.learn.demo.seria;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import yucl.learn.demo.dto.Products;
import yucl.learn.demo.dto.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestProtostuff {

    static Schema<Products> schema = RuntimeSchema.getSchema(Products.class);
    private long userTime;



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
    public static <T> Object[] toObject(InputStream inputStream, Class<T> ... cls ) {
        Object[] objs = new Object[cls.length];
        for(int i=0;i<cls.length;i++) {
            Schema<T> schema = RuntimeSchema.getSchema(cls[i]);
            try {
                T obj = cls[i].newInstance();
                ProtostuffIOUtil.mergeDelimitedFrom(inputStream, obj, schema);
                objs[i] = obj;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objs;
    }

    public static void main(String[] args) throws IOException {
        long time = System.currentTimeMillis();
        for(int i=0;i<100000;i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            User user = new User();
            user.setUserId(101);
            user.setName("xiaolei");
            user.setTags(new String[]{"height", "rich"});
            Products products1 = new Products();
            products1.setI1(1000);
            products1.setB1(true);
            products1.setS1("hello");
            products1.setUser(user);
            Products products2 = new Products();
            products2.setI1(200);
            products2.setUser(user);
            byte[] bytes1 = toByteArray(products1);
            byte[] bytes2 = toByteArray(products2);
            //baos.write(bytes1);
            //baos.write(bytes2);
            ProtostuffIOUtil.writeDelimitedTo(baos, products1, RuntimeSchema.getSchema(Products.class), LinkedBuffer.allocate(512));
            ProtostuffIOUtil.writeDelimitedTo(baos, products2, RuntimeSchema.getSchema(Products.class), LinkedBuffer.allocate(512));
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            // System.out.println(baos.toByteArray().length+":"+baos.size()+":"+bytes1.length+":"+bytes2.length);
            Object[] objects = toObject(bais, Products.class, Products.class);
            //  for(int i=0;i<objects.length;i++)
            //  System.out.println(objects[i]);
        }
        System.out.println(System.currentTimeMillis()-time);

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
