package com.gitee.pifeng.monitoring.common.exception;

/**
 * <p>
 * 自定义获取网络信息异常类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/30 14:07
 */
public class NetException extends MonitoringUniversalException {

    private static final long serialVersionUID = -491704241199412819L;

    public NetException() {
        super();
    }

    public NetException(String message) {
        super(message);
    }
}
