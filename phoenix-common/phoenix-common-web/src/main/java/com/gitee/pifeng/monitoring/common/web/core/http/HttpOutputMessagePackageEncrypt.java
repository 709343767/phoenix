package com.gitee.pifeng.monitoring.common.web.core.http;

import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.util.MsgPayloadUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 处理HTTP输出消息：获取明文数据包，并且加密。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/11 10:43
 */
@Slf4j
public class HttpOutputMessagePackageEncrypt {

    /**
     * <p>
     * 加密数据包
     * </p>
     *
     * @param inputObject 明文数据包
     * @return 密文数据包
     * @author 皮锋
     * @custom.date 2021/4/11 10:57
     */
    public CiphertextPackage encrypt(Object inputObject) {
        // 转成json字符串
        String jsonString = JSON.toJSONString(inputObject);
        // 打印日志
        if (log.isDebugEnabled()) {
            log.debug("响应包：{}", jsonString);
        }
        // 将 明文JSON字符串 转换成 密文数据包
        return MsgPayloadUtils.encryptPayloadTo(jsonString);
    }

}
