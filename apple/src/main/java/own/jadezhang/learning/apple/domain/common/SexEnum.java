package own.jadezhang.learning.apple.domain.common;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public enum SexEnum {

    MALE(0, "男"),
    FEMALE(1, "女"),
    UNKNOWN(2, "未知");

    private final int value;
    private final String name;

    private SexEnum(int value, String name){
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据枚举值获取对应的名称
     *
     * @param value
     * @return
     */
    public static String getNameByValue(int value) {
        SexEnum[] types = SexEnum.values();
        SexEnum type;
        for (int i = 0; i < types.length; i++) {
            type = types[i];
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return "";
    }
}
