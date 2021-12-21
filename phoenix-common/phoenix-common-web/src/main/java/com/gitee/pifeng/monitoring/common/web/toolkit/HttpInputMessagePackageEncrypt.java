package com.gitee.pifeng.monitoring.common.web.toolkit;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.util.ZipUtils;
import com.gitee.pifeng.monitoring.common.util.secure.SecureUtils;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 处理HTTP输出消息：获取明文数据包，并且加密。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/11 10:43
 */
@Slf4j
public class HttpInputMessagePackageEncrypt {

    /**
     * <p>
     * 加密数据包
     * </p>
     *
     * @param inputOjbect 明文数据包
     * @return 密文数据包
     * @author 皮锋
     * @custom.date 2021/4/11 10:57
     */
    public CiphertextPackage encrypt(Object inputOjbect) {
        // 转成json字符串
        String jsonString = JSON.toJSONString(inputOjbect);
        // 打印日志
        log.debug("响应包：{}", jsonString);
        CiphertextPackage requestCiphertextPackage;
        // 字符串是否需要进行gzip压缩
        if (ZipUtils.isNeedGzip(jsonString)) {
            // 压缩
            byte[] gzip = ZipUtil.gzip(jsonString, CharsetUtil.UTF_8);
            // 加密
            String encrypt = SecureUtils.encrypt(gzip, StandardCharsets.UTF_8);
            requestCiphertextPackage = new CiphertextPackage(encrypt, true);
        } else {
            // 加密
            String encrypt = SecureUtils.encrypt(jsonString, StandardCharsets.UTF_8);
            requestCiphertextPackage = new CiphertextPackage(encrypt, false);
        }
        // 密文数据包
        return requestCiphertextPackage;
    }

}
