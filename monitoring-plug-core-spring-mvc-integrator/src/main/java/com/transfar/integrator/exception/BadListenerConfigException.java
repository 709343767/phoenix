package com.transfar.integrator.exception;

/**
 * <p>
 * 错误的监听器配置异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/15 14:30
 */
public class BadListenerConfigException extends Exception {

    /**
	 * serialVersionUID
	 */	
	private static final long serialVersionUID = -1672157366010494089L;

	public BadListenerConfigException() {
        super();
    }

    public BadListenerConfigException(String message) {
        super(message);
    }
}
