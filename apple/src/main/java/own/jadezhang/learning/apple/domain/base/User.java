package own.jadezhang.learning.apple.domain.base;

import own.jadezhang.common.domain.BaseDomain;
import own.jadezhang.learning.apple.view.base.excel.ExcelColProAnnotation;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public class User extends BaseDomain<Long>{
    @ExcelColProAnnotation(columnName = "姓名")
    private String name;
    @ExcelColProAnnotation(columnName = "性别")
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
