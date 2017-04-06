package own.jadezhang.learning.apple.domain.protocol;

/**
 * Created by Zhang Junwei on 2017/4/6 0006.
 */
public class BizHttpResponse {
    /** 返回的响应码 为空，说明是正常返回*/
    private String rtnCode;
    /** 错误信息 有业务异常的时候，来源于BizException；否则网关出错（系统异常），使用通用异常 */
    private String msg;
    /** 错误堆栈信息，便于排查问题   正常是调试模式下该字段才返回信息 */
    private String developMsg;
    /** 错误说明url 有业务异常的时候，来源于BizException；否则网关出错（系统异常），使用通用异常 */
    private String uri;
    private long ts = System.currentTimeMillis();
    /** 返回的业务 有业务异常的时候，来源于BizException；否则网关出错（系统异常），使用通用异常 */
    private Object bizData;



}
