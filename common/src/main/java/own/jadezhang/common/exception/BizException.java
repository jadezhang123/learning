package own.jadezhang.common.exception;

/**
 * 业务异常，为检查时异常，必须捕获
 * ==包装成本异常需要log记录原始msg==
 * Created by Zhang Junwei on 2016/11/6.
 */
public class BizException extends RuntimeException {
    /** 异常码 例如： 0010001 业务异常，业务模块01的0001错误（0业务异常、1系统异常）*/
    private String errorCode;
    /** 对用户友好的错误信息 */
    private String msg;
    /** 错误堆栈信息，便于排查问题 */
    private String developMsg;
    /** 表示这个错误相关的web页面，可以帮助开发人员获取更多的错误的信息 */
    private String uri;


    public BizException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.msg = message;
    }

    public BizException(String errorCode, String message, String developMsg) {
        this(errorCode, message);
        this.developMsg = developMsg;
    }

    public BizException(String errorCode, String message, Throwable cause) {
        this(errorCode, message);
        this.developMsg = cause.getMessage();
    }

    public BizException(String errorCode, String message, String developMsg, String uri) {
        this(errorCode, message, developMsg);
        this.uri = uri;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDevelopMsg() {
        return developMsg;
    }

    public void setDevelopMsg(String developMsg) {
        this.developMsg = developMsg;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "BizException{" +
                "errorCode='" + errorCode + '\'' +
                ", msg='" + msg + '\'' +
                ", developMsg='" + developMsg + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}

