package com.gitee.pifeng.monitoring.common.exception;

/**
 * <p>
 * 监控平台通用异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/26 14:56
 */
public class MonitoringUniversalException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6220209419798968058L;

    public MonitoringUniversalException() {
        super();
    }

    public MonitoringUniversalException(String message) {
        super(message);
    }

}
