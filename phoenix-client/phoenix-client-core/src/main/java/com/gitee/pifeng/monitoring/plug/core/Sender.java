package com.gitee.pifeng.monitoring.plug.core;

import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.util.secure.SecureUtils;
import com.gitee.pifeng.monitoring.plug.util.EnumPoolingHttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * <p>
 * 数据发送者
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午10:20:09
 */
@Slf4j
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
        // 打印发送的数据包
        log.debug("发送数据包：{}", json);
        // 加密请求数据
        String encrypt = SecureUtils.encrypt(json);
        CiphertextPackage requestCiphertextPackage = new CiphertextPackage(encrypt);
        // 发送请求
        EnumPoolingHttpUtils httpClient = EnumPoolingHttpUtils.getInstance();
        String result = httpClient.sendHttpPostByJson(url, requestCiphertextPackage.toJsonString());
        // 响应结果
        CiphertextPackage responseCiphertextPackage = JSON.parseObject(result, CiphertextPackage.class);
        // 解密响应数据
        return SecureUtils.decrypt(responseCiphertextPackage.getCiphertext());
    }

}
