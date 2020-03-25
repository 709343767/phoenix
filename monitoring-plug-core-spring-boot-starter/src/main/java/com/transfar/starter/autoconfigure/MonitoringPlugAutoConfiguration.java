package com.transfar.starter.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.transfar.plug.Monitor;
import com.transfar.starter.annotation.EnableMonitoringPlug;
import com.transfar.starter.exception.BadAnnotateParamException;

import lombok.SneakyThrows;

/**
 * <p>
 * 监控客户端自动配置类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/13 15:40
 */
@Configuration
// 只应用于spring boot项目
@ConditionalOnClass({SpringBootApplication.class})
public class MonitoringPlugAutoConfiguration implements ImportBeanDefinitionRegistrar {

    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(EnableMonitoringPlug.class.getName(), true));
        // 配置文件路径
        assert attributes != null;
        String configFilePath = attributes.getString("configFilePath");
        // 配置文件名字
        String configFileName = attributes.getString("configFileName");
        // 有设置配置文件路径和名字
        if (StringUtils.isNotBlank(configFilePath) && StringUtils.isNotBlank(configFileName)) {
            // 解析配置文件路径
            String path = this.analysisConfigFilePath(configFilePath);
            Monitor.start(path, configFileName);
        } else {
            Monitor.start();
        }
    }

    /**
     * <p>
     * 解析配置文件路径
     * </p>
     *
     * @param configFilePath 配置文件路径
     * @return 解析后的配置文件路径
     * @throws BadAnnotateParamException 错误的注解参数异常
     * @author 皮锋
     * @custom.date 2020年3月15日 下午9:51:46
     */
    private String analysisConfigFilePath(String configFilePath) throws BadAnnotateParamException {
        // 异常信息
        String expMsg = "@EnableMonitoringPlug注解参数有误，请参考如下信息：\r\n"//
                + "@EnableMonitoringPlug(configFilePath = \"classpath:conf/\", configFileName = \"monitoring.properties\")\r\n";
        if (!StringUtils.containsIgnoreCase(configFilePath, "classpath:")) {
            throw new BadAnnotateParamException(expMsg);
        }
        String[] temp = configFilePath.split(":");
        if (temp.length != 2) {
            throw new BadAnnotateParamException(expMsg);
        }
        return temp[1];
    }

}
