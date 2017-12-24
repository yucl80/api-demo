package yucl.learn.demo.samples.test;

import java.util.Arrays;

public class User {
    public int userId;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }

    public String name;
    public String[] tags;
}
