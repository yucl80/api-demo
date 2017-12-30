package yucl.learn.demo.json;

import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import io.protostuff.JsonIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import yucl.learn.demo.dto.Products;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ProtostuffJson {
    public static void main(String[] args){
        Products instance = new Products();
        instance.setI1(100);
        byte[] data=toByteArray(new Object[]{"aa",12});
    }

    public static <T> byte[] toByteArray(T message) {
        Schema<T> schema = RuntimeSchema.getSchema((Class<T>) message.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        byte[] data = JsonIOUtil.toByteArray((T) message, schema, false);
        return data;
    }

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
}
