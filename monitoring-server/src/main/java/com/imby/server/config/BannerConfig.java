package com.imby.server.config;

import com.imby.common.init.InitBanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 配置和打印项目banner。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/16 13:38
 */
@Configuration
public class BannerConfig {

    /**
     * <p>
     * 初始化项目banner
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/9/16 13:39
     */
    @Bean
    public void initBanner() {
        InitBanner.init();
    }
}
