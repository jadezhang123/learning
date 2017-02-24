package own.jadezhang.common.domain;

import org.apache.commons.lang.StringUtils;
import own.jadezhang.common.domain.common.SearchField;
import own.jadezhang.common.domain.enums.MatchTypeEnum;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tengen on 2016/1/7.
 */
public abstract class BaseParam<T> implements Serializable {

    /**
     * 字段常量——主键ID
     */
    public static final String F_ID = "id";
    private static final long serialVersionUID = 1L;
    private T id;

    public T getId() {
        return this.id;
    }

    public void setId(T id) {
        this.id = id;
    }

    /**
     * 转换为普通Map（空值属性不作转换）
     *
     * @return Map
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = StringUtils.uncapitalize(property.getName());
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(this);
                    if (value == null)
                        continue;
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 转换为查询类Map（空值属性不作转换，字符串类型匹配类型默认为精确查询）
     *
     * @return 查询Map
     */
    public Map<String, Object> toSearchFieldMap() {
        return toSearchFieldMap(MatchTypeEnum.EXACT);
    }

    /**
     * 转换为查询类Map（空值属性不作转换）
     *
     * @param matchType 匹配类型（仅对字符串类型且非空字段有效）
     * @return 查询Map
     */
    public Map<String, Object> toSearchFieldMap(MatchTypeEnum matchType) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = StringUtils.uncapitalize(property.getName());
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(this);
                    if (value == null)
                        continue;
                    SearchField searchField = new SearchField(key, "=", value);
                    //字符串前后带%模糊查询
                    if (matchType != null && matchType != MatchTypeEnum.EXACT
                            && (value instanceof String)) {
                        searchField.setOp("like");
                        if (matchType == MatchTypeEnum.LEFT_FUZZY) {
                            searchField.setData("%" + value);
                        } else if (matchType == MatchTypeEnum.RIGHT_FUZZY) {
                            searchField.setData(value + "%");
                        } else if (matchType == MatchTypeEnum.ALL_FUZZY) {
                            searchField.setData("%" + value + "%");
                        }
                    }
                    map.put(key, searchField);
                }
            }
            map.put("groupOp", "and");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
