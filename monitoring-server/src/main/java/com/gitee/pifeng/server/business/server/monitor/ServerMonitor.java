package com.gitee.pifeng.server.business.server.monitor;

import com.gitee.pifeng.common.constant.AlarmLevelEnums;
import com.gitee.pifeng.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.common.domain.Alarm;
import com.gitee.pifeng.common.dto.AlarmPackage;
import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.server.business.server.domain.Server;
import com.gitee.pifeng.server.business.server.pool.ServerPool;
import com.gitee.pifeng.server.business.server.service.IAlarmService;
import com.gitee.pifeng.server.inf.IServerMonitoringListener;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 服务器信息监控
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/17 20:24
 */
@Component
@Slf4j
public class ServerMonitor implements IServerMonitoringListener {

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * 服务器信息池
     */
    @Autowired
    private ServerPool serverPool;

    /**
     * <p>
     * 监控服务器
     * </p>
     *
     * @param obj 回调参数
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/12/17 20:26
     */
    @Override
    public void wakeUpMonitor(Object... obj) throws NetException, SigarException {
        // 是否监控服务器
        boolean isEnable = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().isEnable();
        // 不需要监控服务器
        if (!isEnable) {
            return;
        }
        String key = String.valueOf(obj[0]);
        // 服务器
        Server server = this.serverPool.get(key);
        // 是否首次发现新服务器
        boolean isFirstDiscovery = server.isFirstDiscovery();
        // 首次发现新服务器
        if (isFirstDiscovery) {
            this.sendAlarmInfo("发现新服务器", AlarmLevelEnums.INFO, server);
            server.setFirstDiscovery(false);
        }
        log.info("服务器信息池大小：{}，详细信息：{}", this.serverPool.size(), this.serverPool.toJsonString());
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param server          服务器
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/12/17 20:32
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Server server) throws NetException, SigarException {
        String dateTime = DateTimeFormatter.ofPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue()).format(LocalDateTime.now());
        String msg = "IP地址：" + server.getIp()
                + "，<br>服务器：" + server.getComputerName()
                + "，<br>时间：" + dateTime;
        Alarm alarm = Alarm.builder()
                .title(title)
                .msg(msg)
                .alarmLevel(alarmLevelEnums)
                .monitorType(MonitorTypeEnums.SERVER)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
