package com.gitee.pifeng.monitoring.common.exception;

import java.io.IOException;

/**
 * <p>
 * 解密异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/11/27 14:42
 */
public class DecryptionException extends IOException {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 390375216709615341L;

	public DecryptionException() {
        super();
    }

    public DecryptionException(String message) {
        super(message);
    }

}