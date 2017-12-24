package yucl.learn.demo.samples.test;

import org.codehaus.janino.ExpressionEvaluator;

public class TestJanino {

    public static void main(String[] args){
        try {
            ExpressionEvaluator ee = new ExpressionEvaluator();
            ee.cook("3 + 4");
            System.out.println(ee.evaluate(null)); // Prints "7".
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
