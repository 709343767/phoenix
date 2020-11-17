package com.imby.server.business.server.core;

import cn.hutool.core.util.StrUtil;
import com.imby.server.business.server.entity.*;
import com.imby.server.business.server.property.*;
import com.imby.server.business.server.service.IMonitorConfigService;
import com.imby.server.business.web.service.impl.MonitorConfigServiceImpl;
import com.imby.server.business.web.vo.MonitorConfigPageFormVo;
import com.imby.server.inf.IMonitorConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

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
public class MonitoringConfigPropertiesLoader implements IMonitorConfigListener {

    /**
     * 监控配置属性
     */
    private static MonitoringProperties monitoringProperties;

    /**
     * 监控配置服务接口
     */
    @Autowired
    private IMonitorConfigService monitorConfigService;

    /**
     * <p>
     * 获取监控配置属性
     * </p>
     *
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2020/11/9 22:18
     */
    public static synchronized MonitoringProperties getMonitoringProperties() {
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
    private static synchronized void setMonitoringProperties(MonitoringProperties properties) {
        monitoringProperties = properties;
    }

    /**
     * <p>
     * 项目启动时从数据库中获取监控服务端配置，并保存到静态变量中。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/11/9 22:23
     */
    @Bean
    public void init() {
        setMonitoringProperties(this.loadAllMonitorConfig());
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
        Map<String, Object> allMonitorConfig = this.monitorConfigService.loadAllMonitorConfig();
        MonitorConfigAlarmMail monitorConfigAlarmMail = (MonitorConfigAlarmMail) allMonitorConfig.get("monitorConfigAlarmMail");
        MonitorConfigAlarmSms monitorConfigAlarmSms = (MonitorConfigAlarmSms) allMonitorConfig.get("monitorConfigAlarmSms");
        MonitorConfigAlarm monitorConfigAlarm = (MonitorConfigAlarm) allMonitorConfig.get("monitorConfigAlarm");
        MonitorConfigNet monitorConfigNet = (MonitorConfigNet) allMonitorConfig.get("monitorConfigNet");
        MonitorConfig monitorConfig = (MonitorConfig) allMonitorConfig.get("monitorConfig");
        MonitorConfigServer monitorConfigServer = (MonitorConfigServer) allMonitorConfig.get("monitorConfigServer");
        MonitorConfigServerCpu monitorConfigServerCpu = (MonitorConfigServerCpu) allMonitorConfig.get("monitorConfigServerCpu");
        MonitorConfigServerDisk monitorConfigServerDisk = (MonitorConfigServerDisk) allMonitorConfig.get("monitorConfigServerDisk");
        MonitorConfigServerMemory monitorConfigServerMemory = (MonitorConfigServerMemory) allMonitorConfig.get("monitorConfigServerMemory");
        // 告警邮箱配置属性
        MonitoringAlarmMailProperties monitoringAlarmMailProperties = new MonitoringAlarmMailProperties();
        monitoringAlarmMailProperties.setEmills(StrUtil.splitToArray(monitorConfigAlarmMail.getEmills(), ';'));
        // 告警短信配置属性
        MonitoringAlarmSmsProperties monitoringAlarmSmsProperties = new MonitoringAlarmSmsProperties();
        monitoringAlarmSmsProperties.setPhoneNumbers(StrUtil.splitToArray(monitorConfigAlarmSms.getPhoneNumbers(), ';'));
        monitoringAlarmSmsProperties.setAddress(monitorConfigAlarmSms.getAddress());
        monitoringAlarmSmsProperties.setProtocol(monitorConfigAlarmSms.getProtocol());
        monitoringAlarmSmsProperties.setEnterprise(monitorConfigAlarmSms.getEnterprise());
        // 告警配置属性
        MonitoringAlarmProperties monitoringAlarmProperties = new MonitoringAlarmProperties();
        monitoringAlarmProperties.setEnable(monitorConfigAlarm.getEnable().equals(1));
        monitoringAlarmProperties.setLevel(monitorConfigAlarm.getLevel());
        monitoringAlarmProperties.setWay(StrUtil.splitToArray(monitorConfigAlarm.getWay(), ';'));
        monitoringAlarmProperties.setSmsProperties(monitoringAlarmSmsProperties);
        monitoringAlarmProperties.setMailProperties(monitoringAlarmMailProperties);
        // 网络配置属性
        MonitoringNetworkProperties monitoringNetworkProperties = new MonitoringNetworkProperties();
        monitoringNetworkProperties.setMonitoringEnable(monitorConfigNet.getEnable().equals(1));
        // 服务器CPU配置属性
        MonitoringServerCpuProperties monitoringServerCpuProperties = new MonitoringServerCpuProperties();
        monitoringServerCpuProperties.setOverloadThreshold(monitorConfigServerCpu.getOverloadThreshold());
        // 服务器磁盘配置属性
        MonitoringServerDiskProperties monitoringServerDiskProperties = new MonitoringServerDiskProperties();
        monitoringServerDiskProperties.setOverloadThreshold(monitorConfigServerDisk.getOverloadThreshold());
        // 服务器内存配置属性
        MonitoringServerMemoryProperties monitoringServerMemoryProperties = new MonitoringServerMemoryProperties();
        monitoringServerMemoryProperties.setOverloadThreshold(monitorConfigServerMemory.getOverloadThreshold());
        // 服务器配置属性
        MonitoringServerProperties monitoringServerProperties = new MonitoringServerProperties();
        monitoringServerProperties.setEnable(monitorConfigServer.getEnable().equals(1));
        monitoringServerProperties.setServerCpuProperties(monitoringServerCpuProperties);
        monitoringServerProperties.setServerDiskProperties(monitoringServerDiskProperties);
        monitoringServerProperties.setServerMemoryProperties(monitoringServerMemoryProperties);
        // 监控配置属性
        MonitoringProperties properties = new MonitoringProperties();
        properties.setThreshold(monitorConfig.getThreshold());
        properties.setAlarmProperties(monitoringAlarmProperties);
        properties.setNetworkProperties(monitoringNetworkProperties);
        properties.setServerProperties(monitoringServerProperties);
        log.info("从数据库中加载监控服务端配置成功！");
        return properties;
    }

    /**
     * <p>
     * 更新数据库中的监控配置信息时，唤醒执行监控配置属性加载器回调方法。
     * </p>
     * 此方法在{@link MonitorConfigServiceImpl#update(MonitorConfigPageFormVo)}中被调用。
     *
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    @Override
    public void wakeUpMonitoringConfigPropertiesLoader() {
        setMonitoringProperties(this.loadAllMonitorConfig());
    }

}
