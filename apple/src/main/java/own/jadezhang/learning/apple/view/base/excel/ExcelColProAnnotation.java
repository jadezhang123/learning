package own.jadezhang.learning.apple.view.base.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Zhang Junwei on 2017/1/17 0017.
 * excel 列名与实体字段对应注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelColProAnnotation {
    /**
     * 列名
     *
     * @return
     */
    String columnName() default "";

    /**
     * 是否唯一
     *
     * @return
     */
    boolean isUnique() default false;
}
