package com.gitee.pifeng.monitoring.common.init;

import cn.hutool.setting.dialect.Props;
import com.gitee.pifeng.monitoring.common.property.bootstrap.MonitoringBootstrapProperties;
import com.gitee.pifeng.monitoring.common.property.bootstrap.MonitoringSecretProperties;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 初始化监控引导配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/11/21 20:15
 */
public class InitBootstrapConfig {

    /**
     * 监控引导配置属性
     */
    public static final MonitoringBootstrapProperties MONITORING_BOOTSTRAP_PROPERTIES = load();

    /**
     * <p>
     * 加载监控引导配置信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/11/21 20:44
     */
    private static MonitoringBootstrapProperties load() {
        // 读取“monitoring-bootstrap.properties”文件
        Props props = new Props("monitoring-bootstrap.properties", StandardCharsets.UTF_8);
        // 加解密类型
        String secretType = props.getStr("secret.type");
        // DES密钥
        String secretKeyDes = props.getStr("secret.key.des");
        // AES密钥
        String secretKeyAes = props.getStr("secret.key.aes");
        // 国密SM4密钥
        String secretKeySm4 = props.getStr("secret.key.sm4");
        // 加解密属性
        MonitoringSecretProperties monitoringSecretProperties = new MonitoringSecretProperties();
        monitoringSecretProperties.setSecretType(secretType);
        monitoringSecretProperties.setSecretKeyDes(secretKeyDes);
        monitoringSecretProperties.setSecretKeyAes(secretKeyAes);
        monitoringSecretProperties.setSecretKeySm4(secretKeySm4);
        // 监控引导配置属性
        MonitoringBootstrapProperties monitoringBootstrapProperties = new MonitoringBootstrapProperties();
        monitoringBootstrapProperties.setSecretProperties(monitoringSecretProperties);
        return monitoringBootstrapProperties;
    }

}
