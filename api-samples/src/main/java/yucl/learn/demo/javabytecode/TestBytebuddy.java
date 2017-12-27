package yucl.learn.demo.javabytecode;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TestBytebuddy {
    public static void main(String[] args) {
        TestBytebuddy test = new TestBytebuddy();
        test.f1();
    }

    public void f1() {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();


        try {
            assertThat(dynamicType.newInstance().toString(), is("Hello World!"));
            String str = dynamicType.newInstance().toString();
            System.out.println(str);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

