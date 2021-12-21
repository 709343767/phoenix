package com.gitee.pifeng.monitoring.plug.core;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.util.ZipUtils;
import com.gitee.pifeng.monitoring.common.util.secure.SecureUtils;
import com.gitee.pifeng.monitoring.plug.util.EnumPoolingHttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        CiphertextPackage requestCiphertextPackage;
        // 字符串是否需要进行gzip压缩
        if (ZipUtils.isNeedGzip(json)) {
            // 压缩字符串
            byte[] gzip = ZipUtil.gzip(json, CharsetUtil.UTF_8);
            // 加密请求数据
            String encrypt = SecureUtils.encrypt(gzip, StandardCharsets.UTF_8);
            requestCiphertextPackage = new CiphertextPackage(encrypt, true);
        } else {
            // 加密请求数据
            String encrypt = SecureUtils.encrypt(json, StandardCharsets.UTF_8);
            requestCiphertextPackage = new CiphertextPackage(encrypt, false);
        }
        // 发送请求
        EnumPoolingHttpUtils httpClient = EnumPoolingHttpUtils.getInstance();
        String result = httpClient.sendHttpPostByJson(url, requestCiphertextPackage.toJsonString());
        // 响应结果
        CiphertextPackage responseCiphertextPackage = JSON.parseObject(result, CiphertextPackage.class);
        // 解密响应数据
        String decryptStr;
        // 是否需要进行UnGzip
        boolean gzipEnabled = responseCiphertextPackage.isUnGzipEnabled();
        if (gzipEnabled) {
            // 解密
            byte[] decrypt = SecureUtils.decrypt(responseCiphertextPackage.getCiphertext(), CharsetUtil.UTF_8);
            // 解压
            decryptStr = ZipUtil.unGzip(decrypt, CharsetUtil.UTF_8);
        } else {
            // 解密
            decryptStr = SecureUtils.decrypt(responseCiphertextPackage.getCiphertext(), StandardCharsets.UTF_8);
        }
        return decryptStr;
    }

}
