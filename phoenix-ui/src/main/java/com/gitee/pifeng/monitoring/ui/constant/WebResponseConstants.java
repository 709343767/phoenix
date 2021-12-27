package com.gitee.pifeng.monitoring.ui.constant;

/**
 * <p>
 * 返回给浏览器的常量
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/2 14:46
 */
public class WebResponseConstants {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private WebResponseConstants() {
    }

    /**
     * 成功
     */
    public static final String SUCCESS = "success";

    /**
     * 失败
     */
    public static final String FAIL = "fail";

    /**
     * 已经存在
     */
    public static final String EXIST = "exist";

    /**
     * 校验失败
     */
    public static final String VERIFY_FAIL = "verifyFail";

    /**
     * 刷新失败
     */
    public static final String REFRESH_FAIL = "refreshFail";

    /**
     * 必选项（必填项）为空
     */
    public static final String REQUIRED_IS_NULL = "requiredIsNull";

    /**
     * 数据完整性冲突
     */
    public static final String DATA_INTEGRITY_VIOLATION = "dataIntegrityViolation";

}
