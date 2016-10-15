package own.jadezhang.learning.apple.domain.base;

import own.jadezhang.common.domain.BaseDomain;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public class User extends BaseDomain{
    private String name;
    private int sex;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }
}
