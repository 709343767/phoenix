package com.gitee.pifeng.monitoring.common.exception;

/**
 * <p>
 * 找不到用户异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/9 11:42
 */
public class NotFoundUserException extends MonitoringUniversalException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 403861260545255130L;

    public NotFoundUserException() {
        super();
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}
