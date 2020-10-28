package com.imby.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.common.constant.AlarmLevelEnums;
import com.imby.common.constant.AlarmTypeEnums;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.common.domain.Alarm;
import com.imby.common.dto.AlarmPackage;
import com.imby.common.exception.NetException;
import com.imby.common.util.DateTimeUtils;
import com.imby.common.util.NetUtils;
import com.imby.common.util.OsUtils;
import com.imby.server.business.server.core.PackageConstructor;
import com.imby.server.business.server.domain.Net;
import com.imby.server.business.server.entity.MonitorNet;
import com.imby.server.business.server.pool.NetPool;
import com.imby.server.business.server.service.IAlarmService;
import com.imby.server.business.server.service.INetService;
import com.imby.server.property.MonitoringServerWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.hyperic.sigar.SigarException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
     * 网络信息服务接口
     */
    @Autowired
    private INetService netService;

    /**
     * 延迟/周期执行线程池
     */
    private final ScheduledExecutorService seService = new ScheduledThreadPoolExecutor(5,
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("monitoring-net-pool-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build());

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
        this.seService.scheduleWithFixedDelay(() -> {
            try {
                // 网络监控是否打开
                boolean monitoringEnable = this.monitoringServerWebProperties.getNetworkProperties().isMonitoringEnable();
                // 打开了网络监控
                if (monitoringEnable) {
                    // 循环所有网络信息
                    for (Map.Entry<String, Net> entry : this.netPool.entrySet()) {
                        this.seService.execute(() -> {
                            try {
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
                                    boolean ping = NetUtils.ping(net.getIpSource());
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
                            } catch (Exception e) {
                                log.error("定时扫描网络信息池中的所有IP异常！", e);
                            }
                        });
                    }
                    // 打印当前网络信息池中的所有网络信息情况
                    log.info("当前网络信息池大小：{}，正常：{}，断网：{}，详细信息：{}",
                            this.netPool.size(), //
                            this.netPool.entrySet().stream().filter(e -> e.getValue().isOnConnect()).count(),
                            this.netPool.entrySet().stream().filter(e -> !e.getValue().isOnConnect()).count(),
                            this.netPool.toJsonString());
                }
            } catch (Exception e) {
                log.error("定时扫描网络信息池中的所有IP异常！", e);
            }
        }, 10, 30, TimeUnit.SECONDS);
    }

    /**
     * <p>
     * 处理恢复网络
     * </p>
     *
     * @param net               网络信息
     * @param isRefreshDateTime 是否刷新最后一次更新的时间
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/25 14:20
     */
    private void recoverConnect(Net net, boolean isRefreshDateTime) throws NetException, SigarException {
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
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/25 14:20
     */
    private void disConnect(Net net) throws NetException, SigarException {
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
        // 更新数据库
        this.updateDb(net);
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param net             网络信息
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/25 14:46
     */
    @Async
    public void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Net net) throws NetException, SigarException {
        String dateTime = DateTimeUtils.dateToString(net.getDateTime());
        String msg = "IP地址：" + net.getIpSource() + "到" + net.getIpTarget()
                + "，<br>服务器：" + OsUtils.getComputerName() + "到" + net.getComputerName()
                + "，<br>时间：" + dateTime;
        Alarm alarm = Alarm.builder()
                .title(title)
                .msg(msg)
                .alarmLevel(alarmLevelEnums)
                .alarmType(AlarmTypeEnums.NET)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

    /**
     * <p>
     * 更新数据库中的网络信息
     * </p>
     *
     * @param net 网络信息
     * @author 皮锋
     * @custom.date 2020/8/31 17:23
     */
    private void updateDb(Net net) {
        // IP地址（来源）
        String ipSource = net.getIpSource();
        // IP地址（目的地）
        String ipTarget = net.getIpTarget();
        MonitorNet monitorNet = new MonitorNet();
        monitorNet.setIpSource(ipSource);
        monitorNet.setIpTarget(ipTarget);
        monitorNet.setStatus(ZeroOrOneConstants.ZERO);
        monitorNet.setUpdateTime(new Date());
        LambdaUpdateWrapper<MonitorNet> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorNet::getIpSource, ipSource);
        lambdaUpdateWrapper.eq(MonitorNet::getIpTarget, ipTarget);
        this.netService.updateNet(monitorNet, lambdaUpdateWrapper);
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
