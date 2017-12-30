package yucl.learn.demo.dto;

import java.io.Serializable;
import java.util.Arrays;

public class User implements Serializable{
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    private String name;
    private String[] tags;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
