package com.transfar.server.business.server.core;

import com.transfar.common.constant.AlarmLevelEnums;
import com.transfar.common.domain.Alarm;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.util.NetUtils;
import com.transfar.server.business.server.domain.Net;
import com.transfar.server.business.server.service.IAlarmService;
import com.transfar.server.property.MonitoringServerWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 在容器启动后，定时扫描网络信息池中的所有IP，实时更新状态，发送告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 14:03
 */
@Slf4j
@Component
public class NetCommandLineRunner implements CommandLineRunner, DisposableBean {

    /**
     * 网络信息池
     */
    @Autowired
    private NetPool netPool;

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * 监控配置属性
     */
    @Autowired
    private MonitoringServerWebProperties monitoringServerWebProperties;

    /**
     * 延迟/周期执行线程池
     */
    private final ScheduledExecutorService seService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        AtomicInteger atomic = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "monitoring-net-pool-thread-" + this.atomic.getAndIncrement());
        }
    });

    @Override
    public void run(String... args) {
        // 重新开启线程，让他单独去做我们想要做的操作，此时CommandLineRunner执行的操作和主线程是相互独立的，抛出异常并不会影响到主线程
        Thread thread = new Thread(() -> this.seService.scheduleAtFixedRate(() -> {
            // 网络监控是否打开
            boolean monitoringEnable = this.monitoringServerWebProperties.getNetworkProperties().isMonitoringEnable();
            // 打开了网络监控
            if (monitoringEnable) {
                // 循环所有网络信息
                for (Map.Entry<String, Net> entry : this.netPool.entrySet()) {
                    String key = entry.getKey();
                    Net net = entry.getValue();
                    // 允许的误差时间
                    int thresholdSecond = net.getThresholdSecond();
                    // 最后一次更新的时间
                    Date dateTime = net.getDateTime();
                    // 判决时间
                    DateTime judgeDateTime = new DateTime(dateTime).plusSeconds(thresholdSecond);
                    // 注册上来的IP失去响应
                    if (judgeDateTime.isBeforeNow()) {
                        // 已经断网
                        if (!net.isOnConnect()) {
                            continue;
                        }
                        // 判断网络是不是断了
                        boolean ping = NetUtils.ping(net.getIp());
                        // 网络不通
                        if (!ping) {
                            // 断网
                            this.disConnect(key, net);
                        } else {
                            // 网络恢复连接
                            this.recoverConnect(key, net);
                        }
                    }
                    // 注册上来的IP恢复响应
                    else {
                        // 网络恢复连接
                        this.recoverConnect(key, net);
                    }
                }
                // 打印当前网络信息池中的所有网络信息情况
                log.info("当前网络信息池大小：{}，正常：{}，断网：{}，详细信息：{}", //
                        this.netPool.size(), //
                        this.netPool.entrySet().stream().filter((e) -> e.getValue().isOnConnect()).count(), //
                        this.netPool.entrySet().stream().filter((e) -> !e.getValue().isOnConnect()).count(), //
                        this.netPool.toJsonString());
            }
        }, 45, 30, TimeUnit.SECONDS));
        // 设置守护线程
        thread.setDaemon(true);
        // 开始执行分进程
        thread.start();
    }

    /**
     * <p>
     * 处理恢复网络
     * </p>
     *
     * @param key 网络信息键
     * @param net 网络信息
     * @author 皮锋
     * @custom.date 2020/3/25 14:20
     */
    private void recoverConnect(String key, Net net) {
        // 是否已经发过断网告警信息
        boolean isConnectAlarm = net.isConnectAlarm();
        if (isConnectAlarm) {
            // 发送来网通知信息
            this.sendAlarmInfo("网络恢复", AlarmLevelEnums.WARN, net);
            net.setConnectAlarm(false);
        }
        net.setDateTime(new Date());
        this.netPool.replace(key, net);
    }

    /**
     * <p>
     * 处理断网
     * </p>
     *
     * @param key 网络信息键
     * @param net 网络信息
     * @author 皮锋
     * @custom.date 2020/3/25 14:20
     */
    private void disConnect(String key, Net net) {
        // 断网
        net.setOnConnect(false);
        // 是否已经发过断网告警信息
        boolean isConnectAlarm = net.isConnectAlarm();
        // 没发送断网告警
        if (!isConnectAlarm) {
            // 发送断网告警信息
            this.sendAlarmInfo("网络中断", AlarmLevelEnums.FATAL, net);
            net.setConnectAlarm(true);
        }
        this.netPool.replace(key, net);
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param net             网络信息
     * @author 皮锋
     * @custom.date 2020/3/25 14:46
     */
    private synchronized void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Net net) {
        new Thread(() -> {
            Instant instant = net.getDateTime().toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
            String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
            String msg = "IP地址：" + NetUtils.getLocalIp() + "到" + net.getIp() + "，时间：" + dateTime;
            Alarm alarm = Alarm.builder()//
                    .title(title)//
                    .msg(msg)//
                    .alarmLevel(alarmLevelEnums)//
                    .build();
            AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
            alarmService.dealAlarmPackage(alarmPackage);
        }).start();
    }

    /**
     * <p>
     * 在spring容器销毁时关闭线程池
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/3/26 9:57
     */
    @Override
    public void destroy() {
        if (!this.seService.isShutdown()) {
            this.seService.shutdown();
            log.info("延迟/周期执行线程池“monitoring-net-pool-thread”已经关闭！");
        }
    }
}
