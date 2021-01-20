package com.gitee.pifeng.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gitee.pifeng.common.constant.AlarmLevelEnums;
import com.gitee.pifeng.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.common.domain.Alarm;
import com.gitee.pifeng.common.dto.AlarmPackage;
import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.common.threadpool.ThreadPool;
import com.gitee.pifeng.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.server.business.server.domain.Server;
import com.gitee.pifeng.server.business.server.entity.MonitorServer;
import com.gitee.pifeng.server.business.server.pool.ServerPool;
import com.gitee.pifeng.server.business.server.service.IAlarmService;
import com.gitee.pifeng.server.business.server.service.IServerService;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
@Order(2)
public class ServerMonitorTask implements CommandLineRunner {

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
     * 服务器信息服务层接口
     */
    @Autowired
    private IServerService serverService;

    /**
     * <p>
     * 项目启动完成后延迟5秒钟启动定时任务，扫描服务器信息池中的所有服务器信息，实时更新服务器信息状态，发送告警，
     * 然后在一次执行结束和下一次执行开始之间延迟30秒。
     * </p>
     *
     * @param args 传入的主方法参数
     * @author 皮锋
     * @custom.date 2020/4/7 22:00
     */
    @Override
    public void run(String... args) {
        ThreadPool.COMMON_IO_INTENSIVE_SCHEDULED_THREAD_POOL.scheduleWithFixedDelay(() -> {
            try {
                // 是否监控服务器
                boolean isEnable = MonitoringConfigPropertiesLoader.getMonitoringProperties().getServerProperties().isEnable();
                // 不需要监控服务器
                if (!isEnable) {
                    return;
                }
                // 循环所有服务器信息
                for (Map.Entry<String, Server> entry : this.serverPool.entrySet()) {
                    Server server = entry.getValue();
                    // 允许的误差时间
                    int thresholdSecond = server.getThresholdSecond();
                    // 最后一次通过服务器信息包更新的时间
                    Date dateTime = server.getDateTime();
                    // 判决时间
                    DateTime judgeDateTime = new DateTime(dateTime).plusSeconds(thresholdSecond);
                    // 注册上来的服务器失去响应
                    if (judgeDateTime.isBeforeNow()) {
                        // 已经离线
                        if (!server.isOnline()) {
                            continue;
                        }
                        // 离线
                        this.offLine(server);
                    }
                    // 注册上来的服务器恢复响应
                    else {
                        if (server.isFirstDiscovery()) {
                            // 首次发现新服务器
                            this.firstDiscovery(server);
                        } else {
                            // 恢复在线
                            this.onLine(server);
                        }
                    }
                }
                // 打印当前服务器信息池中的所有服务器情况
                log.info("服务器信息池大小：{}，在线：{}，离线：{}，详细信息：{}",
                        this.serverPool.size(),
                        this.serverPool.entrySet().stream().filter(e -> e.getValue().isOnline()).count(),
                        this.serverPool.entrySet().stream().filter(e -> !e.getValue().isOnline()).count(),
                        this.serverPool.toJsonString());
            } catch (Exception e) {
                log.error("定时扫描服务器信息池中的所有服务器信息异常！", e);
            }
        }, 5, 30, TimeUnit.SECONDS);
    }

    /**
     * <p>
     * 发现新的服务器
     * </p>
     *
     * @param server 服务器
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/12/17 15:14
     */
    private void firstDiscovery(Server server) throws NetException, SigarException {
        // 发送发现新的服务器通知信息
        this.sendAlarmInfo("发现新服务器", AlarmLevelEnums.INFO, server);
        server.setFirstDiscovery(false);
        server.setLineAlarm(false);
    }

    /**
     * <p>
     * 处理恢复在线
     * </p>
     *
     * @param server 服务器
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/23 11:58
     */
    private void onLine(Server server) throws NetException, SigarException {
        server.setOnline(true);
        // 是否已经发过离线告警信息
        boolean isLineAlarm = server.isLineAlarm();
        if (isLineAlarm) {
            // 发送在线通知信息
            this.sendAlarmInfo("服务器上线", AlarmLevelEnums.INFO, server);
            server.setLineAlarm(false);
        }
    }

    /**
     * <p>
     * 处理离线
     * </p>
     *
     * @param server 服务器
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2021/1/20 20:40
     */
    private void offLine(Server server) throws NetException, SigarException {
        // 离线
        server.setOnline(false);
        // 是否已经发过离线告警信息
        boolean isLineAlarm = server.isLineAlarm();
        // 没发送离线告警
        if (!isLineAlarm) {
            // 发送离线告警信息
            this.sendAlarmInfo("服务器离线", AlarmLevelEnums.FATAL, server);
            server.setLineAlarm(true);
        }
        // 更新数据库
        this.updateDb(server);
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
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Server server) throws
            NetException, SigarException {
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

    /**
     * <p>
     * 更新数据库中的服务器信息
     * </p>
     *
     * @param server 服务器
     * @author 皮锋
     * @custom.date 2020/5/11 10:18
     */
    private void updateDb(Server server) {
        boolean isOnline = server.isOnline();
        MonitorServer monitorServer = new MonitorServer();
        monitorServer.setUpdateTime(new Date());
        // 离线
        if (!isOnline) {
            monitorServer.setIsOnline(ZeroOrOneConstants.ZERO);
        }
        LambdaUpdateWrapper<MonitorServer> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorServer::getIp, server.getIp());
        this.serverService.updateInstance(monitorServer, lambdaUpdateWrapper);
    }

}
