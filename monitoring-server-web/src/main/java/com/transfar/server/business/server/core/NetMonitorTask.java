package com.transfar.server.business.server.core;

import com.transfar.common.constant.AlarmLevelEnums;
import com.transfar.common.constant.AlarmTypeEnums;
import com.transfar.common.domain.Alarm;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.util.DateTimeUtils;
import com.transfar.common.util.NetUtils;
import com.transfar.common.util.SigarUtils;
import com.transfar.server.business.server.domain.Net;
import com.transfar.server.business.server.service.IAlarmService;
import com.transfar.server.property.MonitoringServerWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 在项目启动后，定时扫描网络信息池中的所有IP，实时更新状态，发送告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 14:03
 */
@Slf4j
@Component
@Order(2)
public class NetMonitorTask implements CommandLineRunner, DisposableBean {

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
    private final ScheduledExecutorService seService = Executors.newScheduledThreadPool(100, new ThreadFactory() {
        AtomicInteger atomic = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "monitoring-net-pool-thread-" + this.atomic.getAndIncrement());
        }
    });

    /**
     * <p>
     * 项目启动完成后延迟10秒钟启动定时任务，扫描网络信息池中的所有IP，实时更新状态，发送告警，
     * 然后在一次执行结束和下一次执行开始之间延迟30秒。
     * </p>
     *
     * @param args 传入的主方法参数
     * @author 皮锋
     * @custom.date 2020/4/7 22:03
     */
    @Override
    public void run(String... args) {
        // 重新开启线程，让他单独去做我们想要做的操作，此时CommandLineRunner执行的操作和主线程是相互独立的，抛出异常并不会影响到主线程
        Thread thread = new Thread(() -> this.seService.scheduleWithFixedDelay(() -> {
            try {
                // 网络监控是否打开
                boolean monitoringEnable = this.monitoringServerWebProperties.getNetworkProperties().isMonitoringEnable();
                // 打开了网络监控
                if (monitoringEnable) {
                    // 循环所有网络信息
                    for (Map.Entry<String, Net> entry : this.netPool.entrySet()) {
                        this.seService.execute(() -> {
                            Net net = entry.getValue();
                            // 允许的误差时间
                            int thresholdSecond = net.getThresholdSecond();
                            // 最后一次更新的时间
                            Date dateTime = net.getDateTime();
                            // 判决时间
                            DateTime judgeDateTime = new DateTime(dateTime).plusSeconds(thresholdSecond);
                            // 注册上来的IP失去响应
                            if (judgeDateTime.isBeforeNow()) {
                                // 已经断网，也需要继续判断，防止没有应用向服务端发心跳包，这种情况要主动ping
                                // if (!net.isOnConnect()) {
                                // continue;
                                // }
                                // 判断网络是不是断了
                                boolean ping = NetUtils.ping(net.getIp());
                                // 网络不通
                                if (!ping) {
                                    // 断网
                                    this.disConnect(net);
                                } else {
                                    // 网络恢复连接
                                    this.recoverConnect(net, true);
                                }
                            }
                            // 注册上来的IP恢复响应
                            else {
                                // 网络恢复连接
                                this.recoverConnect(net, false);
                            }
                        });
                    }
                    // 打印当前网络信息池中的所有网络信息情况
                    log.info("当前网络信息池大小：{}，正常：{}，断网：{}，详细信息：{}", //
                            this.netPool.size(), //
                            this.netPool.entrySet().stream().filter((e) -> e.getValue().isOnConnect()).count(), //
                            this.netPool.entrySet().stream().filter((e) -> !e.getValue().isOnConnect()).count(), //
                            this.netPool.toJsonString());
                }
            } catch (Exception e) {
                log.error("定时扫描网络信息池中的所有IP异常！", e);
            }
        }, 10, 30, TimeUnit.SECONDS));
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
     * @param net               网络信息
     * @param isRefreshDateTime 是否刷新最后一次更新的时间
     * @author 皮锋
     * @custom.date 2020/3/25 14:20
     */
    private void recoverConnect(Net net, boolean isRefreshDateTime) {
        net.setOnConnect(true);
        // 是否已经发过断网告警信息
        boolean isConnectAlarm = net.isConnectAlarm();
        if (isConnectAlarm) {
            // 发送来网通知信息
            this.sendAlarmInfo("网络恢复", AlarmLevelEnums.FATAL, net);
            net.setConnectAlarm(false);
        }
        // 是否刷新最后一次更新的时间
        if (isRefreshDateTime) {
            net.setDateTime(new Date());
        }
    }

    /**
     * <p>
     * 处理断网
     * </p>
     *
     * @param net 网络信息
     * @author 皮锋
     * @custom.date 2020/3/25 14:20
     */
    private void disConnect(Net net) {
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
    @Async
    public void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Net net) {
        String dateTime = DateTimeUtils.dateToString(net.getDateTime());
        String msg = "IP地址：" + NetUtils.getLocalIp() + "到" + net.getIp()
                + "，服务器：" + SigarUtils.getComputerName() + "到" + net.getComputerName()
                + "，时间：" + dateTime;
        Alarm alarm = Alarm.builder()//
                .title(title)//
                .msg(msg)//
                .alarmLevel(alarmLevelEnums)//
                .alarmType(AlarmTypeEnums.NET)//
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

    /**
     * <p>
     * 在spring容器销毁时关闭线程池
     * </p>
     * 关闭线程池：monitoring-net-pool-thread
     *
     * @author 皮锋
     * @custom.date 2020/3/26 9:57
     */
    @Override
    public void destroy() {
        if (!this.seService.isShutdown()) {
            this.seService.shutdown();
            log.info("周期执行线程池“monitoring-net-pool-thread”已经关闭！");
        }
    }
}
