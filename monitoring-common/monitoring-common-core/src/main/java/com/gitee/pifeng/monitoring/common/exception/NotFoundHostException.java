package com.gitee.pifeng.monitoring.common.exception;

/**
 * <p>
 * 找不到主机异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午7:25:39
 */
public class NotFoundHostException extends MonitoringUniversalException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3983006251850390541L;

    public NotFoundHostException() {
        super();
    }

    public NotFoundHostException(String message) {
        super(message);
    }

}
