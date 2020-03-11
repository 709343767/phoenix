package com.transfar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.transfar.config.UniqueBeanNameGenerator;

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
// @EnableScheduling
// 启用事务管理
public class AgentApplication {

    /**
     * 配置应用上下文
     */
    public static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(AgentApplication.class, args);
    }

}
