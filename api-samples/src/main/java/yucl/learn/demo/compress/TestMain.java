package yucl.learn.demo.compress;

import yucl.learn.demo.dto.Products;
import yucl.learn.demo.seria.TestJavaSeria;

public class TestMain {
    public static void main(String[] args){
        byte[] bytes = TestJavaSeria.toByteArray(new Products());
        System.out.println(bytes.length);
        CompressUtil compressUtil = new LZ4();
        long begin= System.currentTimeMillis();
        byte[] rst = new byte[0];
        for(int i=0;i<100000;i++){
            byte[] compressBytes = compressUtil.compress(bytes);
            rst = compressUtil.decompress(compressBytes);
            if(bytes.length != rst.length){
                System.out.println("ERROR");
            }
        }
        System.out.println(System.currentTimeMillis()-begin);
        TestJavaSeria.toObject(rst,Products.class);


    }
}
