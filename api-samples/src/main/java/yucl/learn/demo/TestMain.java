package yucl.learn.demo;

import yucl.learn.demo.dto.Products;
import yucl.learn.demo.dto.User;
import yucl.learn.demo.seria.TestJavaSeria;
import yucl.learn.demo.seria.TestKryo;
import yucl.learn.demo.seria.TestProtostuff;

public class TestMain {

    public static void main(String[] args) {
        User user = new User();
        user.setUserId(101);
        user.setName("xiaolei");
        user.setTags(new String[]{"height", "rich"});
        Products products = new Products();
        products.setS1("hello");
        products.setB1(true);
        products.setUser(user);
        long total = System.currentTimeMillis();
        int count = 500000;
        for (int j = 0; j < 2000; j++) {
            {
                long l = System.currentTimeMillis();
                for (int i = 0; i < count; i++) {
                    //String a="aa";
                    // int b=12;
                    // byte[] bytes = TestKryo.toByteArray(new Object[]{a,b});
                    // Object[] objs = TestKryo.toObject(bytes,Object[].class);
                    // System.out.println(new String(bytes));
                    //  System.out.println(objs[0]+" "+objs[1]);
                    byte[] bytes = TestJavaSeria.toByteArray(products);
                    Products x = TestJavaSeria.toObject(bytes, Products.class);
                    // System.out.print(new String(bytes));
                    //*TestProtostuff.f4();*/
                    //TestProtostuff.f1();
                }
                System.out.print(System.currentTimeMillis() - l);
            }
            System.out.print(":");
            {
                long l = System.currentTimeMillis();
                for (int i = 0; i < count; i++) {
                    byte[] bytes = TestProtostuff.toByteArray(products);
                    Products x = TestProtostuff.toObject(bytes, Products.class);
                }
                System.out.print(System.currentTimeMillis() - l);
            }
            System.out.print(":");
            {
                long l = System.currentTimeMillis();
                for (int i = 0; i < count; i++) {
                    byte[] bytes = TestKryo.toByteArray(products);
                    Products x = TestKryo.toObject(bytes, Products.class);
                }
                System.out.println(System.currentTimeMillis() - l);
            }
        }
        System.out.println(System.currentTimeMillis() - total);
    }
}
