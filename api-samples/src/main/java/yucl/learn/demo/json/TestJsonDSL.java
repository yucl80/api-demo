package yucl.learn.demo.json;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;

import java.io.*;

public class TestJsonDSL {
    static DslJson<Object> dslJson = new DslJson<Object>();

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            f2();
        }
        System.out.println(System.currentTimeMillis() - l);
        //vv();
    }


    private static void f2() {
        Pojo instance = new Pojo();
        instance.setNumber(100);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(instance);
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
            Pojo pojo = (Pojo) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void f1() {
        try {

           /* Pojo instance = new Pojo();
            instance.setNumber(100);
            JsonWriter writer2 = dslJson.newWriter();
            dslJson.serialize(writer2, instance);
            Pojo deser2 = dslJson.deserialize(Pojo.class, writer2.getByteBuffer(), writer2.size());*/

            Pojo2 instance2 = new Pojo2();
            instance2.setNumber(100);
            instance2.setName("test");
            JsonWriter writer3 = dslJson.newWriter();
            dslJson.serialize(writer3, instance2);
            Pojo2 deser3 = dslJson.deserialize(Pojo2.class, writer3.getByteBuffer(), writer3.size());

            // System.out.println(deser2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void vv() {
        try {
            DslJson<Object> json = new DslJson<Object>(); //always reuse

            byte[] bytes = "{\"number\":123}".getBytes("UTF-8");
            JsonReader<Object> reader = json.newReader().process(bytes, bytes.length);
            Pojo instance = new Pojo(); //can be reused
            Pojo bound = reader.next(Pojo.class, instance); //bound is the same as instance above
            System.out.println(bound);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @CompiledJson
    public static class Pojo implements Serializable {
        public int getNumber() {
            return number;
        }

        @Override
        public String toString() {
            return "Pojo{" +
                    "number=" + number +
                    '}';
        }

        public void setNumber(int number) {
            this.number = number;
        }

        private int number;

    }

    @CompiledJson
    public static class Pojo2 implements Serializable {
        public int getNumber() {
            return number;
        }


        public void setNumber(int number) {
            this.number = number;
        }

        private int number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;

        @Override
        public String toString() {
            return "Pojo2{" +
                    "number=" + number +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
