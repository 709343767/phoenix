package com.transfar.common.inf;

/**
 * <p>
 * 回调接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/30 20:17
 */
public interface ICallback {

    /**
     * <p>
     * 回调方法
     * </p>
     *
     * @param obj 回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    default void event(Object... obj) {
    }

    /**
     * <p>
     * 回调方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/3/30 20:27
     */
    default void event() {
    }

}
