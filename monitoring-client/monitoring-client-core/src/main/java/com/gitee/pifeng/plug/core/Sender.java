package com.gitee.pifeng.plug.core;

import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.common.dto.CiphertextPackage;
import com.gitee.pifeng.common.util.DesEncryptUtils;
import com.gitee.pifeng.plug.util.EnumHttpUtils;

import java.io.IOException;

/**
 * <p>
 * 数据发送者
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午10:20:09
 */
public class Sender {
    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private Sender() {
    }


    /**
     * <p>
     * 发送数据
     * </p>
     *
     * @param url  URL地址
     * @param json JSON字符串格式的数据
     * @return 服务端或者代理端返回值
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2020年3月6日 上午10:21:25
     */
    public static String send(final String url, final String json) throws IOException {
        // 加密请求数据
        String encrypt = DesEncryptUtils.encrypt(json);
        CiphertextPackage requestCiphertextPackage = new CiphertextPackage(encrypt);
        // 发送请求
        EnumHttpUtils httpClient = EnumHttpUtils.getInstance();
        String result = httpClient.sendHttpPostByJson(url, requestCiphertextPackage.toJsonString());
        // 响应结果
        CiphertextPackage responseCiphertextPackage = JSON.parseObject(result, CiphertextPackage.class);
        // 解密响应数据
        return DesEncryptUtils.decrypt(responseCiphertextPackage.getCiphertext());
    }

}
