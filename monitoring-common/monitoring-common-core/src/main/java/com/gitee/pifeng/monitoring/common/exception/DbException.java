package com.gitee.pifeng.monitoring.common.exception;

/**
 * <p>
 * 自定义数据库异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/19 17:46
 */
public class DbException extends MonitoringUniversalException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6047087509207781738L;

    public DbException() {
        super();
    }

    public DbException(String message) {
        super(message);
    }

}
