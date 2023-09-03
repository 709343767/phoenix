package com.gitee.pifeng.monitoring.server.business.server.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gitee.pifeng.monitoring.common.constant.CommProtocolTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.EnterpriseEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.property.server.*;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorConfig;
import com.gitee.pifeng.monitoring.server.business.server.service.IConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * <p>
 * 监控配置属性加载器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/23 14:13
 */
@Slf4j
@Component
public class MonitoringConfigPropertiesLoader {

    /**
     * 监控配置属性
     */
    private static MonitoringProperties monitoringProperties;

    /**
     * 监控配置服务接口
     */
    @Autowired
    private IConfigService configService;

    /**
     * <p>
     * 获取监控配置属性
     * </p>
     *
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2020/11/9 22:18
     */
    public MonitoringProperties getMonitoringProperties() {
        return monitoringProperties;
    }

    /**
     * <p>
     * 设置监控配置属性
     * </p>
     *
     * @param properties {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2020/11/10 8:43
     */
    private synchronized void setMonitoringProperties(MonitoringProperties properties) {
        monitoringProperties = properties;
        log.info("设置到监控配置属性对象成功，属性详情：{}", monitoringProperties.toJsonString());
    }

    /**
     * <p>
     * 项目启动时从数据库中获取监控服务端配置，并保存到静态变量中。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/11/9 22:23
     */
    @PostConstruct
    public void init() {
        // 获取监控配置属性
        MonitoringProperties properties = this.loadAllMonitorConfig();
        this.setMonitoringProperties(properties);
        log.info("加载监控服务端配置成功！");
    }

    /**
     * <p>
     * 获取监控配置属性
     * </p>
     *
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2020/11/4 13:01
     */
    public MonitoringProperties loadAllMonitorConfig() {
        // 加载监控配置
        MonitorConfig monitorConfig = this.configService.getOne(new LambdaQueryWrapper<>());
        if (monitorConfig == null) {
            return this.setDefaultMonitorConfig();
        }
        // 监控配置值
        String value = monitorConfig.getValue();
        if (value == null) {
            return this.setDefaultMonitorConfig();
        }
        Feature[] feature = new Feature[]{Feature.AllowComment, Feature.AllowUnQuotedFieldNames, Feature.AllowSingleQuotes};
        try {
            return JSON.parseObject(value, MonitoringProperties.class, feature);
        } catch (Exception e) {
            return this.setDefaultMonitorConfig();
        }
    }

    /**
     * <p>
     * 设置默认监控配置属性
     * </p>
     *
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2021/1/28 20:21
     */
    private MonitoringProperties setDefaultMonitorConfig() {
        // 告警邮箱配置属性
        MonitoringAlarmMailProperties alarmMailProperties = new MonitoringAlarmMailProperties();
        // 告警短信配置属性
        MonitoringAlarmSmsProperties alarmSmsProperties = new MonitoringAlarmSmsProperties(null, null, CommProtocolTypeEnums.HTTP, EnterpriseEnums.PHOENIX);
        // 告警配置属性
        MonitoringAlarmProperties alarmProperties = new MonitoringAlarmProperties(false,
                AlarmLevelEnums.INFO,
                new AlarmWayEnums[]{AlarmWayEnums.MAIL, AlarmWayEnums.SMS},
                alarmSmsProperties,
                alarmMailProperties);
        // 网络配置属性
        MonitoringNetworkProperties networkProperties = new MonitoringNetworkProperties(true);
        // TCP配置属性
        MonitoringTcpProperties tcpProperties = new MonitoringTcpProperties(true);
        // HTTP配置属性
        MonitoringHttpProperties httpProperties = new MonitoringHttpProperties(true);
        // 服务器CPU配置属性
        MonitoringServerCpuProperties serverCpuProperties = new MonitoringServerCpuProperties(90D, AlarmLevelEnums.INFO);
        // 服务器磁盘配置属性
        MonitoringServerDiskProperties serverDiskProperties = new MonitoringServerDiskProperties(90D, AlarmLevelEnums.INFO);
        // 服务器内存配置属性
        MonitoringServerMemoryProperties serverMemoryProperties = new MonitoringServerMemoryProperties(90D, AlarmLevelEnums.INFO);
        // 服务器平均负载配置属性
        MonitoringServerLoadAverageProperties serverLoadAverageProperties = new MonitoringServerLoadAverageProperties(1D, AlarmLevelEnums.INFO);
        // 服务器配置属性
        MonitoringServerProperties serverProperties = new MonitoringServerProperties(true, serverCpuProperties, serverDiskProperties, serverMemoryProperties, serverLoadAverageProperties);
        // 数据库表空间配置属性
        MonitoringDbTableSpaceProperties dbTableSpaceProperties = new MonitoringDbTableSpaceProperties(90D, AlarmLevelEnums.INFO);
        // 数据库配置
        MonitoringDbProperties dbProperties = new MonitoringDbProperties(true, dbTableSpaceProperties);
        // 监控配置属性
        MonitoringProperties properties = new MonitoringProperties(5, alarmProperties, networkProperties, tcpProperties, httpProperties, serverProperties, dbProperties);
        // 查询数据库中是否有配置记录
        MonitorConfig monitorConfig = this.configService.getOne(new LambdaQueryWrapper<>());
        if (monitorConfig == null) {
            // 插入记录
            this.configService.save(MonitorConfig.builder().value(properties.toJsonString()).insertTime(new Date()).build());
        } else {
            // 更新记录
            this.configService.update(MonitorConfig.builder().value(properties.toJsonString()).updateTime(new Date()).build(),
                    new LambdaUpdateWrapper<MonitorConfig>().eq(MonitorConfig::getId, monitorConfig.getId()));
        }
        return properties;
    }

    /**
     * <p>
     * 首次执行延迟5分钟，然后每5分钟从数据库获取一次最新的配置信息，更新内存中的配置。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    @Scheduled(initialDelay = 300000, fixedDelay = 300000)
    public void wakeUpMonitoringConfigPropertiesLoader() {
        this.setMonitoringProperties(this.loadAllMonitorConfig());
    }

}
