package com.transfar.starter.exception;

/**
 * <p>
 * 错误的注解参数异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月15日 下午9:39:41
 */
public class BadAnnotateParamException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1628816165436676764L;

	public BadAnnotateParamException() {
		super();
	}

	public BadAnnotateParamException(String message) {
		super(message);
	}

}
