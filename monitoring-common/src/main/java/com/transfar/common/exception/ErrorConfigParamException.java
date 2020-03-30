package com.transfar.common.exception;

/**
 * <p>
 * 错误的配置参数异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/30 9:45
 */
public class ErrorConfigParamException extends Exception {
    
    /**
	 * serialVersionUID
	 */	
	private static final long serialVersionUID = -4146067533755697316L;

	public ErrorConfigParamException() {
        super();
    }

    public ErrorConfigParamException(String message) {
        super(message);
    }
}
