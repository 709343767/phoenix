package com.gitee.pifeng.common.exception;

/**
 * <p>
 * 找不到配置文件异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午10:53:13
 */
public class NotFoundConfigFileException extends Exception {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -792375440969840360L;

    public NotFoundConfigFileException() {
        super();
    }

    public NotFoundConfigFileException(String message) {
        super(message);
    }

}
