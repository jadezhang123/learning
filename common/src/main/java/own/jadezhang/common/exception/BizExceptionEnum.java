package own.jadezhang.common.exception;

/**
 * Created by Zhang Junwei on 2016/11/6.
 */
public enum BizExceptionEnum {
    /** 不能为空 */
    REQUIRED("0008888", "不能为空"),
    /** 特定长度 */
    LENGTH("0007777", "长度必须为："),
    /** 最大长度 */
    MAXLENGTH("0009999", "不能超过长度："),
    /** 邮箱地址 */
    ISEMAIL("0009010", "不是正确的邮箱格式"),
    /** IP地址 */
    ISIPV4("0009020", "不是正确的IP地址"),
    /** 整数 */
    ISINT("0009030", "不是正确的整数"),
    /** 正整数 */
    ISINTPOS("0009031", "不是正确的正整数"),
    /** 数字 */
    ISNUM("0009031", "不是正确的数字"),
    /** 是URL */
    ISURL("0009040", "不是正确的URL"),
    /** 是中文 */
    ISZW("0009050", "不是正确的中文"),
    /** 邮编 */
    ISPOSTCODE("0009060", "不是正确的邮编"),


    /** 已经存在 */
    EXISTS("0005555", " 所填写信息不唯一"),
    /** 不存在 */
    NOTEXISTS("0004444", "不存在"),
    /** 新增失败 */
    ADDERROR("0003333", "新增失败"),
    /** 修改失败 */
    UPDATEERROR("0002222", "修改失败"),
    /** 删除失败 */
    DELERROR("0001111", "删除失败"),
    /** 没有相应的操作权限 */
    DENY("0001112", "没有相应的操作权限"),
    /** 登录验权失败 */
    AUTHERROR("0001113", "登录验权失败");


    private String code;
    private String desc;

    private BizExceptionEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

