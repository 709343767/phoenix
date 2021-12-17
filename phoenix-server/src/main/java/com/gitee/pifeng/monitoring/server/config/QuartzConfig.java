package com.gitee.pifeng.monitoring.server.config;

import com.gitee.pifeng.monitoring.server.business.server.monitor.*;
import org.joda.time.DateTime;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Quartz的JobDetail和Trigger配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/12/1 16:41
 */
@Configuration
public class QuartzConfig {

    /**
     * JobDetail分组
     */
    private static final String JOB_DETAIL_GROUP = "monitoringJobGroup";

    /**
     * Trigger分组
     */
    private static final String TRIGGER_GROUP = "monitoringTriggerGroup";

    /////////////////////////////////////////////instanceMonitor start//////////////////////////////////////////////////

    /**
     * <p>
     * 应用实例状态监控 JobDetail 配置
     * </p>
     *
     * @return 传递给定作业实例的详细信息属性
     * @author 皮锋
     * @custom.date 2021/12/3 12:29
     */
    @Bean
    public JobDetail instanceMonitorJobDetail() {
        return JobBuilder.newJob(InstanceMonitorJob.class)
                .withIdentity("instanceMonitorJob", JOB_DETAIL_GROUP)
                .storeDurably()
                .build();
    }

    /**
     * <p>
     * 应用实例状态监控 Trigger 配置
     * </p>
     *
     * @return 具有所有触发器通用属性的基本接口
     * @author 皮锋
     * @custom.date 2021/12/1 17:31
     */
    @Bean
    public Trigger instanceMonitorTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(this.instanceMonitorJobDetail())
                .withIdentity("instanceMonitorTrigger", TRIGGER_GROUP)
                // 项目启动完成后延迟5秒钟启动定时任务，然后每30秒钟执行一次
                .startAt(new DateTime().plusSeconds(5).toDate())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever())
                .build();
    }
    /////////////////////////////////////////////instanceMonitor end////////////////////////////////////////////////////

    /////////////////////////////////////////////serverMonitor start////////////////////////////////////////////////////

    /**
     * <p>
     * 服务器状态监控 JobDetail 配置
     * </p>
     *
     * @return 传递给定作业实例的详细信息属性
     * @author 皮锋
     * @custom.date 2021/12/3 12:29
     */
    @Bean
    public JobDetail serverMonitorJobDetail() {
        return JobBuilder.newJob(ServerMonitorJob.class)
                .withIdentity("serverMonitorJob", JOB_DETAIL_GROUP)
                .storeDurably()
                .build();
    }

    /**
     * <p>
     * 服务器状态监控 Trigger 配置
     * </p>
     *
     * @return 具有所有触发器通用属性的基本接口
     * @author 皮锋
     * @custom.date 2021/12/1 17:31
     */
    @Bean
    public Trigger serverMonitorTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(this.serverMonitorJobDetail())
                .withIdentity("serverMonitorTrigger", TRIGGER_GROUP)
                // 项目启动完成后延迟5秒钟启动定时任务，然后每30秒钟执行一次
                .startAt(new DateTime().plusSeconds(5).toDate())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(30).repeatForever())
                .build();
    }
    /////////////////////////////////////////////serverMonitor end//////////////////////////////////////////////////////

    ///////////////////////////////////////////////////dbMonitor start//////////////////////////////////////////////////

    /**
     * <p>
     * 数据库监控 JobDetail 配置
     * </p>
     *
     * @return 传递给定作业实例的详细信息属性
     * @author 皮锋
     * @custom.date 2021/12/3 12:29
     */
    @Bean
    public JobDetail dbMonitorJobDetail() {
        return JobBuilder.newJob(DbMonitorJob.class)
                .withIdentity("dbMonitorJob", JOB_DETAIL_GROUP)
                .storeDurably()
                .build();
    }

    /**
     * <p>
     * 数据库监控 Trigger 配置
     * </p>
     *
     * @return 具有所有触发器通用属性的基本接口
     * @author 皮锋
     * @custom.date 2021/12/1 17:31
     */
    @Bean
    public Trigger dbMonitorTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(this.dbMonitorJobDetail())
                .withIdentity("dbMonitorTrigger", TRIGGER_GROUP)
                // 项目启动完成后延迟10秒钟启动定时任务，然后每5分钟执行一次
                .startAt(new DateTime().plusSeconds(10).toDate())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).repeatForever())
                .build();
    }
    ///////////////////////////////////////////////////dbMonitor end////////////////////////////////////////////////////

    /////////////////////////////////////////////dbTableSpaceMonitor start//////////////////////////////////////////////

    /**
     * <p>
     * 数据库表空间监控 JobDetail 配置
     * </p>
     *
     * @return 传递给定作业实例的详细信息属性
     * @author 皮锋
     * @custom.date 2021/12/3 12:29
     */
    @Bean
    public JobDetail dbTableSpaceMonitorJobDetail() {
        return JobBuilder.newJob(DbTableSpaceMonitorJob.class)
                .withIdentity("dbTableSpaceMonitorJob", JOB_DETAIL_GROUP)
                .storeDurably()
                .build();
    }

    /**
     * <p>
     * 数据库表空间监控 Trigger 配置
     * </p>
     *
     * @return 具有所有触发器通用属性的基本接口
     * @author 皮锋
     * @custom.date 2021/12/1 17:31
     */
    @Bean
    public Trigger dbTableSpaceMonitorTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(this.dbTableSpaceMonitorJobDetail())
                .withIdentity("dbTableSpaceMonitorTrigger", TRIGGER_GROUP)
                // 每天早上8点执行一次
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8 * * ?"))
                //.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();
    }
    /////////////////////////////////////////////dbTableSpaceMonitor end////////////////////////////////////////////////

    /////////////////////////////////////////////netMonitor start///////////////////////////////////////////////////////

    /**
     * <p>
     * 网络状态监控 JobDetail 配置
     * </p>
     *
     * @return 传递给定作业实例的详细信息属性
     * @author 皮锋
     * @custom.date 2021/12/3 12:29
     */
    @Bean
    public JobDetail netMonitorJobDetail() {
        return JobBuilder.newJob(NetMonitorJob.class)
                .withIdentity("netMonitorJob", JOB_DETAIL_GROUP)
                .storeDurably()
                .build();
    }

    /**
     * <p>
     * 网络状态监控 Trigger 配置
     * </p>
     *
     * @return 具有所有触发器通用属性的基本接口
     * @author 皮锋
     * @custom.date 2021/12/1 17:31
     */
    @Bean
    public Trigger netMonitorTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(this.netMonitorJobDetail())
                .withIdentity("netMonitorTrigger", TRIGGER_GROUP)
                // 项目启动完成后延迟10秒钟启动定时任务，然后每5分钟执行一次
                .startAt(new DateTime().plusSeconds(10).toDate())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(5).repeatForever())
                .build();
    }
    /////////////////////////////////////////////netMonitor end/////////////////////////////////////////////////////////

    //////////////////////////////////////////ClearHistoryData start////////////////////////////////////////////////////

    /**
     * <p>
     * 数据库历史记录表数据清理 JobDetail 配置
     * </p>
     *
     * @return 传递给定作业实例的详细信息属性
     * @author 皮锋
     * @custom.date 2021/12/3 12:29
     */
    @Bean
    public JobDetail clearHistoryDataJobDetail() {
        return JobBuilder.newJob(ClearHistoryDataJob.class)
                .withIdentity("clearHistoryDataJob", JOB_DETAIL_GROUP)
                .storeDurably()
                .build();
    }

    /**
     * <p>
     * 数据库历史记录表数据清理 Trigger 配置
     * </p>
     *
     * @return 具有所有触发器通用属性的基本接口
     * @author 皮锋
     * @custom.date 2021/12/1 17:31
     */
    @Bean
    public Trigger clearHistoryDataTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(this.clearHistoryDataJobDetail())
                .withIdentity("clearHistoryDataTrigger", TRIGGER_GROUP)
                // 每小时执行一次
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?"))
                .build();
    }
    //////////////////////////////////////////ClearHistoryData end//////////////////////////////////////////////////////
}
