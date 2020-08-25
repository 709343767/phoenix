package com.imby.server;

import com.imby.common.web.toolkit.CustomizationBeanHandler;
import com.imby.common.web.toolkit.UniqueBeanNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * 本SpringApplication类提供了一个方便的方式来引导该从Spring应用程序的main()方法开始。<br>
 * 在许多情况下，您可以委托给静态SpringApplication.run方法
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年1月20日 上午8:45:29
 */
@SpringBootApplication
@ComponentScan(nameGenerator = UniqueBeanNameGenerator.class)
// 启用spring的任务调度
@EnableScheduling
@EnableAsync
// 启用事务管理
@EnableTransactionManagement
public class ServerWebApplication extends CustomizationBeanHandler {

    public static void main(String[] args) {
        SpringApplication.run(ServerWebApplication.class, args);
    }

}
