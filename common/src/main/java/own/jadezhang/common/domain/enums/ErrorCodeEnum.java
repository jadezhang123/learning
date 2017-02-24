package own.jadezhang.common.domain.enums;

/**
 */
public enum ErrorCodeEnum {

	LOGIN_TIMEOUT("0000001", "登录超时，请重新登录!"),
	PARAM_ERROR("0100001", "参数错误"),
	PARAM_NULL("0100002", "参数不能为空"),
	PARAM_TOOLONG("0100003", "参数长度超过限制");

	/** The code. */
	private final String code;

	/** The message. */
	private final String message;

	/**
	 * Instantiates a new error type.
	 * 
	 * @param code
	 *            the code
	 * @param message
	 *            the message
	 */
	private ErrorCodeEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}