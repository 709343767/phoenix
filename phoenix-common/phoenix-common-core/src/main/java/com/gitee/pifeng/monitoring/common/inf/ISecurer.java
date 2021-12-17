package com.gitee.pifeng.monitoring.common.inf;

/**
 * <p>
 * 加解密接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/13 11:57
 */
public interface ISecurer {

    /**
     * <p>
     * 字符串加密
     * </p>
     *
     * @param str 需要加密的字符串
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/13 11:11
     */
    String encrypt(String str);

    /**
     * <p>
     * 字符串解密
     * </p>
     *
     * @param str 需要解密的字符串
     * @return 解密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/13 11:11
     */
    String decrypt(String str);

}
