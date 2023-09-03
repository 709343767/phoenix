package com.gitee.pifeng.monitoring.ui.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * <p>
 * 自定义验证码校验失败异常
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/3/21 8:44
 */
public class VerificationCodeException extends AuthenticationException {

    public VerificationCodeException() {
        super(VerificationCodeExceptionEnums.VERIFICATION_FAILED.getMsg());
    }

    public VerificationCodeException(VerificationCodeExceptionEnums verificationCodeExceptionEnums) {
        super(verificationCodeExceptionEnums.getMsg());
    }

    /**
     * <p>
     * 验证码异常枚举
     * </p>
     *
     * @author 皮锋
     * @custom.date 2023/3/22 9:23
     */
    @Getter
    @AllArgsConstructor
    public enum VerificationCodeExceptionEnums {

        /**
         * 图形验证码不能为空
         */
        CANNOT_BE_EMPTY("001", "图形验证码不能为空！"),

        /**
         * 图形验证码不存在
         */
        DOES_NOT_EXIST("002", "图形验证码不存在！"),

        /**
         * 图形验证码已过期
         */
        HAS_EXPIRED("003", "图形验证码已过期！"),

        /**
         * 图形验证码校验失败
         */
        VERIFICATION_FAILED("004", "图形验证码校验失败！");

        private String code;

        private String msg;

    }

}
