package yucl.learn.demo.samples.test;

public class TestMain {

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {

            Products products = new Products();
            products.setS1("hello");
            products.setB1(true);
            byte[] bytes = TestProtostuff.toByteArray(products);
            Products x = TestProtostuff.toObject(bytes, Products.class);
          /*  byte[] bytes =TestJavaSeria.toByteArray(products);
            Products x = TestJavaSeria.toObject(bytes,Products.class);*/
            //*TestProtostuff.f4();*/
            //TestProtostuff.f1();

        }
        System.out.println(System.currentTimeMillis() - l);

    }
}
