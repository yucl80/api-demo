package yucl.learn.demo.json;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.DecodingMode;
import yucl.learn.demo.dto.Products;
import yucl.learn.demo.dto.User;

import java.io.IOException;

public class TestJsoniter {

    public static void main(String[] args){
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
        Products products = new Products();
        products.setS1("hello");
        products.setB1(true);
        for (int j = 0; j < 20; j++) {
            long l = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                String str = JsonStream.serialize(products);
                JsonIterator.deserialize(str, Products.class);
            }
            System.out.println(System.currentTimeMillis() - l);
        }
       //f3();
       //System.out.println(str);
    }

    public static  void f2(){
        Any obj = JsonIterator.deserialize("[1,2,3]");
        System.out.println(obj.get(2));
        int[] array = JsonIterator.deserialize("[1,2,3]", int[].class);
        System.out.println(array[2]);
    }

    public static void f3(){

       try {
           JsonIterator iter = JsonIterator.parse("[123, {'name': 'taowen', 'tags': ['crazy', 'hacker']}]".replace('\'', '"'));
           iter.readArray();
           int userId = iter.readInt();
           iter.readArray();
           User user = iter.read(User.class);
           user.setUserId(userId);
           iter.readArray(); // end of array
           System.out.println(user);
       }catch (IOException e){
            e.printStackTrace();
       }
    }

    public static void f1(){
        JsonStream.serialize(new int[]{1,2,3}); // from object to JSON
        JsonIterator.deserialize("[1,2,3]", int[].class);
    }
}
