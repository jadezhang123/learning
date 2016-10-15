package own.jadezhang.common.domain.common;

import java.io.Serializable;

/**
 * 返回结果
 */
public class ResultDTO implements Serializable {
    //成功失败
    private boolean isSuccess;
    //信息
    private String msg;
    //值
    private Object value;

    public ResultDTO(boolean isSuccess, String msg, Object value) {
        this.isSuccess = isSuccess;
        this.msg = msg;
        this.value = value;
    }

    public ResultDTO(boolean isSuccess, Object value) {
        this.isSuccess = isSuccess;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ResultDTO(boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
