package com.gitee.pifeng.monitoring.starter.autoconfigure;

import com.gitee.pifeng.monitoring.common.exception.BadAnnotateParamException;
import com.gitee.pifeng.monitoring.plug.Monitor;
import com.gitee.pifeng.monitoring.starter.annotation.EnableMonitoring;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <p>
 * 监控客户端自动配置类。
 * </p>
 * 1.最低优先级；<br>
 * 2.只应用于spring boot项目。<br>
 *
 * @author 皮锋
 * @custom.date 2020/3/13 15:40
 */
@Configuration
@ConditionalOnClass(Monitor.class)
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class MonitoringPlugAutoConfiguration implements ImportAware {

    /**
     * <p>
     * 开启监控
     * </p>
     *
     * @param importMetadata 注释元数据
     * @author 皮锋
     * @custom.date 2020/4/11 22:26
     */
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableMonitoring.class.getName(), true));
        // 配置文件路径
        assert attributes != null;
        String configFilePath = attributes.getString("configFilePath");
        // 配置文件名字
        String configFileName = attributes.getString("configFileName");
        // 开启监控
        if (StringUtils.isBlank(configFilePath) && StringUtils.isBlank(configFileName)) {
            Monitor.start();
        } else if (StringUtils.isNotBlank(configFilePath)) {
            // 解析配置文件路径
            String path = this.analysisConfigFilePath(configFilePath);
            Monitor.start(path, configFileName);
        } else {
            Monitor.start(configFilePath, configFileName);
        }
    }

    /**
     * <p>
     * 解析配置文件路径
     * </p>
     *
     * @param configFilePath 配置文件路径
     * @return 解析后的配置文件路径
     * @author 皮锋
     * @custom.date 2020年3月15日 下午9:51:46
     */
    @SneakyThrows
    private String analysisConfigFilePath(String configFilePath) {
        if (!StringUtils.startsWith(configFilePath, "classpath:") && !StringUtils.startsWith(configFilePath, "filepath:")) {
            // 异常信息
            String expMsg = "\r\n@EnableMonitoring 注解参数有误，请参考如下配置：\r\n"
                    + "@EnableMonitoring(configFilePath = \"classpath:conf/\", configFileName = \"monitoring.properties\", usingMonitoringConfigFile = true)\r\n";
            throw new BadAnnotateParamException(expMsg);
        }
        return configFilePath;
    }

}
