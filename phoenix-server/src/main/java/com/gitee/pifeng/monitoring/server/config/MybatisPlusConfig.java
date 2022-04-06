package com.gitee.pifeng.monitoring.server.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.gitee.pifeng.monitoring.common.web.toolkit.UniqueBeanNameGenerator;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

/**
 * <p>
 * 配置MybatisPlus
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年1月20日 下午3:25:28
 */
@Configuration
@Slf4j
@MapperScan(value = {"com.gitee.pifeng.monitoring.server.business.*.dao"}, nameGenerator = UniqueBeanNameGenerator.class)
public class MybatisPlusConfig {

    /**
     * <p>
     * 配置MybatisPlus分页插件
     * </p>
     *
     * @return {@link PaginationInterceptor}
     * @author 皮锋
     * @custom.date 2020年1月20日 下午3:26:09
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求，默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启count的join优化,只针对部分left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        log.info("MybatisPlus分页插件配置成功！");
        return paginationInterceptor;
    }

    /**
     * <p>
     * 配置MyBatis分页插件PageHelper
     * </p>
     *
     * @return {@link PageHelper}
     * @author 皮锋
     * @custom.date 2020/2/12 10:22
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        // 默认值为 false，该参数对使用RowBounds作为分页参数时有效。
        // 当该参数设置为 true 时，会将RowBounds中的offset参数当成pageNum使用，可以用页码和页面大小两个参数进行分页。
        properties.setProperty("offsetAsPageNum", "true");
        // 默认值为false，该参数对使用RowBounds作为分页参数时有效。
        // 当该参数设置为true时，使用RowBounds分页会进行count查询。
        properties.setProperty("rowBoundsWithCount", "true");
        // 分页合理化参数，默认值为false。
        // 当该参数设置为true时，pageNum<=0时会查询第一页，pageNum>pages（超过总数时），会查询最后一页。
        // 默认false时，直接根据参数进行查询。
        properties.setProperty("reasonable", "false");
        pageHelper.setProperties(properties);
        log.info("MyBatis分页插件PageHelper配置成功！");
        return pageHelper;
    }

    /**
     * <p>
     * 配置自定义SQL注入器
     * </p>
     *
     * @return {@link EasySqlInjector}
     * @author 皮锋
     * @custom.date 2022/4/6 9:07
     */
    @Bean
    public EasySqlInjector easySqlInjector() {
        EasySqlInjector easySqlInjector = new EasySqlInjector();
        log.info("自定义SQL注入器配置成功！");
        return easySqlInjector;
    }

    /**
     * <p>
     * 配置MybatisPlus SQL执行效率插件
     * </p>
     *
     * @return {@link PerformanceInterceptor}
     * @author 皮锋
     * @custom.date 2020/2/12 9:43
     */
    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        log.info("MybatisPlus SQL执行效率插件配置成功！");
        return performanceInterceptor;
    }

}