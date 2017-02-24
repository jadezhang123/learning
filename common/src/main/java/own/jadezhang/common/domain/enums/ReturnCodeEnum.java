package own.jadezhang.common.domain.enums;

/**
 *
 */
public enum ReturnCodeEnum {

	/**
	 * 操作提示
	 */
	SUCCESS_COMPLETE("0100001","操作成功"),
	DELETE_COMPLETE("0100002","删除成功"),
	ADD_COMPLETE("0100003","新增成功"),
	UPDATE_COMPLETE("0100004","更新成功"),
	ACTION_FAILURE("0100005", "操作失败"),
	;
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
	private ReturnCodeEnum(String code, String message) {
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