package com.gitee.pifeng.monitoring.common.inf;

import java.nio.charset.Charset;

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
     * @param str     需要加密的字符串
     * @param charset 字符集
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/13 11:11
     */
    String encrypt(String str, Charset charset);

    /**
     * <p>
     * 字节数组加密，返回字符串
     * </p>
     *
     * @param arry 需要加密的字节数组
     * @return 加密后的字符串
     * @author 皮锋
     * @custom.date 2021/12/19 22:39
     */
    String encrypt(byte[] arry);

    /**
     * <p>
     * 字符串解密
     * </p>
     *
     * @param str     需要解密的字符串
     * @param charset 字符集
     * @return 解密后的字符串
     * @author 皮锋
     * @custom.date 2021/8/13 11:11
     */
    String decrypt(String str, Charset charset);

    /**
     * <p>
     * 字符串解密，返回字节数组
     * </p>
     *
     * @param str 需要解密的字符串
     * @return 解密后的字节数组
     * @author 皮锋
     * @custom.date 2021/12/21 12:50
     */
    byte[] decrypt(String str);

}
