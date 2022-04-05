package com.gitee.pifeng.monitoring.server;

import com.gitee.pifeng.monitoring.common.web.toolkit.CustomizationBeanHandler;
import com.gitee.pifeng.monitoring.common.web.toolkit.UniqueBeanNameGenerator;
import com.gitee.pifeng.monitoring.starter.annotation.EnableMonitoring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * 本SpringApplication类提供了一个方便的方式来引导该从Spring应用程序的main()方法开始。<br>
 * 在许多情况下，您可以委托给静态SpringApplication.run方法。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年1月20日 上午8:45:29
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan(nameGenerator = UniqueBeanNameGenerator.class)
@EnableTransactionManagement
@EnableMonitoring
@EnableRetry
public class ServerApplication extends CustomizationBeanHandler {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
