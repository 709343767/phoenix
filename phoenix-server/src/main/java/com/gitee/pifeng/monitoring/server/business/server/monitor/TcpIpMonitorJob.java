package com.gitee.pifeng.monitoring.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.*;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcpIp;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.ITcpIpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 在项目启动后，定时扫描数据库“MONITOR_TCPIP”表中的所有TCP/IP信息，更新TCP/IP服务状态，发送告警。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/11 16:00
 */
@Slf4j
@Component
@Order(7)
public class TcpIpMonitorJob extends QuartzJobBean {

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * TCP/IP信息服务接口
     */
    @Autowired
    private ITcpIpService tcpIpService;

    /**
     * <p>
     * 扫描数据库“MONITOR_TCPIP”表中的所有TCP/IP信息，实时更新TCP/IP服务状态，发送告警。
     * </p>
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2022/1/11 16:31
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        // 是否监控TCP/IP服务
        boolean isMonitoringEnable = MonitoringConfigPropertiesLoader.getMonitoringProperties().getTcpIpProperties().isEnable();
        if (!isMonitoringEnable) {
            return;
        }
        try {
            // 获取TCP/IP信息列表
            List<MonitorTcpIp> monitorTcpIps = this.tcpIpService.list(new LambdaQueryWrapper<MonitorTcpIp>().eq(MonitorTcpIp::getIpSource, NetUtils.getLocalIp()));
            // 循环处理每一个TCP/IP信息
            for (MonitorTcpIp monitorTcpIp : monitorTcpIps) {
                // 目标IP地址
                String ipTarget = monitorTcpIp.getIpTarget();
                // 目标端口号
                Integer portTarget = monitorTcpIp.getPortTarget();
                // 协议
                String protocol = monitorTcpIp.getProtocol();
                TcpIpEnums protocolEnum = TcpIpEnums.str2Enum(protocol);
                // 使用多线程，加快处理速度
                ThreadPoolExecutor threadPoolExecutor = ThreadPool.COMMON_IO_INTENSIVE_THREAD_POOL;
                threadPoolExecutor.execute(() -> {
                    // 测试telnet能否成功
                    Map<String, Object> telnet = NetUtils.telnetVT200(ipTarget, portTarget, protocolEnum);
                    boolean isConnected = Boolean.parseBoolean(String.valueOf(telnet.get("isConnect")));
                    // TCP/IP服务正常
                    if (isConnected) {
                        // 处理TCP/IP服务正常
                        this.connected(monitorTcpIp);
                    }
                    // TCP/IP服务异常
                    else {
                        // 处理TCP/IP服务异常
                        this.disconnected(monitorTcpIp);
                    }
                });
            }
        } catch (NetException e) {
            log.error("定时扫描数据库“MONITOR_TCPIP”表中的所有TCP/IP信息异常！", e);
        }
    }

    /**
     * <p>
     * 处理TCP/IP服务异常
     * </p>
     *
     * @param monitorTcpIp TCP/IP信息表
     * @author 皮锋
     * @custom.date 2022/1/12 11:30
     */
    private void disconnected(MonitorTcpIp monitorTcpIp) {
        try {
            this.sendAlarmInfo("TCP/IP服务中断", AlarmLevelEnums.FATAL, AlarmReasonEnums.NORMAL_2_ABNORMAL, monitorTcpIp);
        } catch (Exception e) {
            log.error("TCP/IP服务告警异常！", e);
        }
        // 原本是在线或者未知
        String status = monitorTcpIp.getStatus();
        if (StringUtils.equalsIgnoreCase(status, ZeroOrOneConstants.ONE) || StringUtils.isBlank(status)) {
            // 离线次数 +1
            int offlineCount = monitorTcpIp.getOfflineCount() == null ? 0 : monitorTcpIp.getOfflineCount();
            monitorTcpIp.setOfflineCount(offlineCount + 1);
        }
        monitorTcpIp.setStatus(ZeroOrOneConstants.ZERO);
        monitorTcpIp.setUpdateTime(new Date());
        // 更新数据库
        this.tcpIpService.updateById(monitorTcpIp);
    }

    /**
     * <p>
     * 处理TCP/IP服务正常
     * </p>
     *
     * @param monitorTcpIp TCP/IP信息表
     * @author 皮锋
     * @custom.date 2022/1/12 11:27
     */
    private void connected(MonitorTcpIp monitorTcpIp) {
        try {
            if (StringUtils.isNotBlank(monitorTcpIp.getStatus())) {
                this.sendAlarmInfo("TCP/IP服务恢复", AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL, monitorTcpIp);
            }
        } catch (Exception e) {
            log.error("TCP/IP服务告警异常！", e);
        }
        monitorTcpIp.setStatus(ZeroOrOneConstants.ONE);
        monitorTcpIp.setUpdateTime(new Date());
        // 更新数据库
        this.tcpIpService.updateById(monitorTcpIp);
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnum  告警级别
     * @param alarmReasonEnum 告警原因
     * @param monitorTcpIp    TCP/IP信息
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2022/1/12 11:33
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum, MonitorTcpIp monitorTcpIp) throws NetException, SigarException {
        StringBuilder builder = new StringBuilder();
        builder.append("源IP：").append(monitorTcpIp.getIpSource())
                .append("，<br>目标IP：").append(monitorTcpIp.getIpTarget())
                .append("，<br>目标端口：").append(monitorTcpIp.getPortTarget())
                .append("，<br>协议：").append(monitorTcpIp.getProtocol());
        if (StringUtils.isNotBlank(monitorTcpIp.getDescr())) {
            builder.append("，<br>描述：").append(monitorTcpIp.getDescr());
        }
        builder.append("，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                // 保证code的唯一性
                .code(Md5Utils.encrypt32(monitorTcpIp.getIpSource() + monitorTcpIp.getIpTarget() + monitorTcpIp.getPortTarget()))
                .title(title)
                .msg(builder.toString())
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(MonitorTypeEnums.TCPIP4SERVICE)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
