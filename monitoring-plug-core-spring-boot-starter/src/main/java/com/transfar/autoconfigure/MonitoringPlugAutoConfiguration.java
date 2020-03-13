package com.transfar.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import com.transfar.Monitor;

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
@ConditionalOnClass({ SpringBootApplication.class })
public class MonitoringPlugAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		// 开启监控
		Monitor.start();
	}

}
