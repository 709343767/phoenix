package com.gitee.pifeng.monitoring.agent;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.gitee.pifeng.monitoring.common.web.core.CustomizationUndertowBeanHandler;
import com.gitee.pifeng.monitoring.common.web.core.UniqueBeanNameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Indexed;

/**
 * <p>
 * 本SpringApplication类提供了一个方便的方式来引导该从Spring应用程序的main()方法开始。<br>
 * 在许多情况下，您可以委托给静态SpringApplication.run方法
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年1月20日 上午8:45:29
 */
@Slf4j
@Indexed
@EnableRetry
@SpringBootApplication
@ComponentScan(nameGenerator = UniqueBeanNameGenerator.class)
public class AgentApplication extends CustomizationUndertowBeanHandler {

    public static void main(String[] args) {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        SpringApplication.run(AgentApplication.class, args);
        // 时间差（毫秒）
        String betweenDay = timer.intervalPretty();
        log.info("监控代理端启动耗时：{}", betweenDay);
    }

}
