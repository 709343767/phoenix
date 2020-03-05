package com.transfar.exception;

/**
 * <p>
 * 找不到配置信息异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午10:37:44
 */
public class NotFoundConfigException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6546543050835301134L;

	public NotFoundConfigException() {
		super();
	}

	public NotFoundConfigException(String message) {
		super(message);
	}

}
