package com.transfar.autoconfigure;

import com.transfar.Monitor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

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
// @Slf4j
public class MonitoringPlugAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        // 开启监控
        Monitor.start();
        // log.info("监控功能开启成功！");
    }

}
