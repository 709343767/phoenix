package com.gitee.pifeng.monitoring.common.init;

import cn.hutool.setting.dialect.Props;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 初始化加解密配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/11/22 12:18
 */
@Slf4j
public class InitSecure {

    /**
     * 加解密配置属性
     */
    private static final Props PROPS = init();

    /**
     * <p>
     * 读取“monitoring-secure.properties”文件
     * </p>
     *
     * @return 加解密配置属性
     * @author 皮锋
     * @custom.date 2021/11/22 12:37
     */
    private static Props init() {
        Props props = new Props("monitoring-secure.properties", StandardCharsets.UTF_8);
        // 加解密类型
        String secretType = props.getStr("secret.type");
        log.info("初始化加解密配置成功！加解密类型：{}", StringUtils.isNotBlank(secretType) ? secretType : "无");
        return props;
    }

    /**
     * 加解密类型
     */
    public static final String SECRET_TYPE = PROPS.getStr("secret.type");

    /**
     * DES密钥
     */
    public static final String SECRET_KEY_DES = PROPS.getStr("secret.key.des");

    /**
     * AES密钥
     */
    public static final String SECRET_KEY_AES = PROPS.getStr("secret.key.aes");

    /**
     * 国密SM4密钥
     */
    public static final String SECRET_KEY_SM4 = PROPS.getStr("secret.key.sm4");

}
