package com.gitee.pifeng.monitoring.common.exception;

/**
 * <p>
 * 找不到配置参数异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午10:37:44
 */
public class NotFoundConfigParamException extends MonitoringUniversalException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6546543050835301134L;

    public NotFoundConfigParamException() {
        super();
    }

    public NotFoundConfigParamException(String message) {
        super(message);
    }

}
