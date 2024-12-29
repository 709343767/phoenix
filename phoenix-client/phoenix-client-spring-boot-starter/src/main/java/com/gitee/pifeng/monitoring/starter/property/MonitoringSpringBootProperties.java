package com.gitee.pifeng.monitoring.starter.property;

import com.gitee.pifeng.monitoring.common.property.client.MonitoringProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * SpringBoot监控属性，继承自{@link MonitoringProperties}，因为他们的属性是一模一样的。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午3:24:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component("monitoringSpringBootProperties")
@ConfigurationProperties(prefix = "phoenix.monitoring")
public class MonitoringSpringBootProperties extends MonitoringProperties {
}
