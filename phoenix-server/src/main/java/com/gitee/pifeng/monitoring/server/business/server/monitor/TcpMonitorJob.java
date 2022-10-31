package com.gitee.pifeng.monitoring.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcp;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcpHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.ITcpHistoryService;
import com.gitee.pifeng.monitoring.server.business.server.service.ITcpService;
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
 * 在项目启动后，定时扫描数据库“MONITOR_TCP”表中的所有TCP信息，更新TCP服务状态，发送告警。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/11 16:00
 */
@Slf4j
@Component
@Order(7)
public class TcpMonitorJob extends QuartzJobBean {

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * TCP信息服务接口
     */
    @Autowired
    private ITcpService tcpService;

    /**
     * TCP信息历史记录服务类
     */
    @Autowired
    private ITcpHistoryService tcpHistoryService;

    /**
     * <p>
     * 扫描数据库“MONITOR_TCP”表中的所有TCP信息，实时更新TCP服务状态，发送告警。
     * </p>
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2022/1/11 16:31
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        // 是否监控TCP服务
        boolean isMonitoringEnable = MonitoringConfigPropertiesLoader.getMonitoringProperties().getTcpProperties().isEnable();
        if (!isMonitoringEnable) {
            return;
        }
        try {
            // 获取TCP信息列表
            List<MonitorTcp> monitorTcps = this.tcpService.list(new LambdaQueryWrapper<MonitorTcp>().eq(MonitorTcp::getHostnameSource, NetUtils.getLocalIp()));
            // 循环处理每一个TCP信息
            for (MonitorTcp monitorTcp : monitorTcps) {
                // 目标主机名
                String hostnameTarget = monitorTcp.getHostnameTarget();
                // 目标端口号
                Integer portTarget = monitorTcp.getPortTarget();
                // 使用多线程，加快处理速度
                ThreadPoolExecutor threadPoolExecutor = ThreadPool.COMMON_IO_INTENSIVE_THREAD_POOL;
                threadPoolExecutor.execute(() -> {
                    // 测试telnet能否成功
                    Map<String, Object> telnet = NetUtils.telnetVT200(hostnameTarget, portTarget);
                    // 是否能telnet通
                    boolean isConnected = Boolean.parseBoolean(String.valueOf(telnet.get("isConnect")));
                    // 平均时间（毫秒）
                    Long avgTime = Long.valueOf(String.valueOf(telnet.get("avgTime")));
                    // TCP服务正常
                    if (isConnected) {
                        // 处理TCP服务正常
                        this.connected(monitorTcp, avgTime);
                    }
                    // TCP服务异常
                    else {
                        // 处理TCP服务异常
                        this.disconnected(monitorTcp, avgTime);
                    }
                });
            }
        } catch (NetException e) {
            log.error("定时扫描数据库“MONITOR_TCP”表中的所有TCP信息异常！", e);
        }
    }

    /**
     * <p>
     * 处理TCP服务异常
     * </p>
     *
     * @param monitorTcp TCP信息表
     * @param avgTime    平均时间（毫秒）
     * @author 皮锋
     * @custom.date 2022/1/12 11:30
     */
    private void disconnected(MonitorTcp monitorTcp, Long avgTime) {
        try {
            this.sendAlarmInfo("TCP服务中断", AlarmLevelEnums.FATAL, AlarmReasonEnums.NORMAL_2_ABNORMAL, monitorTcp);
        } catch (Exception e) {
            log.error("TCP服务告警异常！", e);
        }
        // 原本是在线或者未知
        String status = monitorTcp.getStatus();
        if (StringUtils.equalsIgnoreCase(status, ZeroOrOneConstants.ONE) || StringUtils.isBlank(status)) {
            // 离线次数 +1
            int offlineCount = monitorTcp.getOfflineCount() == null ? 0 : monitorTcp.getOfflineCount();
            monitorTcp.setOfflineCount(offlineCount + 1);
        }
        Date date = new Date();
        monitorTcp.setStatus(ZeroOrOneConstants.ZERO);
        monitorTcp.setAvgTime(avgTime);
        monitorTcp.setUpdateTime(date);
        // 更新数据库
        this.tcpService.updateById(monitorTcp);
        // 添加历史记录
        MonitorTcpHistory monitorTcpHistory = new MonitorTcpHistory();
        monitorTcpHistory.setTcpId(monitorTcp.getId());
        monitorTcpHistory.setHostnameSource(monitorTcp.getHostnameSource());
        monitorTcpHistory.setHostnameTarget(monitorTcp.getHostnameTarget());
        monitorTcpHistory.setPortTarget(monitorTcp.getPortTarget());
        monitorTcpHistory.setDescr(monitorTcp.getDescr());
        monitorTcpHistory.setStatus(monitorTcp.getStatus());
        monitorTcpHistory.setAvgTime(monitorTcp.getAvgTime());
        monitorTcpHistory.setOfflineCount(monitorTcp.getOfflineCount());
        monitorTcpHistory.setInsertTime(date);
        monitorTcpHistory.setUpdateTime(date);
        this.tcpHistoryService.save(monitorTcpHistory);
    }

    /**
     * <p>
     * 处理TCP服务正常
     * </p>
     *
     * @param monitorTcp TCP信息表
     * @param avgTime    平均时间（毫秒）
     * @author 皮锋
     * @custom.date 2022/1/12 11:27
     */
    private void connected(MonitorTcp monitorTcp, Long avgTime) {
        try {
            if (StringUtils.isNotBlank(monitorTcp.getStatus())) {
                this.sendAlarmInfo("TCP服务恢复", AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL, monitorTcp);
            }
        } catch (Exception e) {
            log.error("TCP服务告警异常！", e);
        }
        Date date = new Date();
        monitorTcp.setStatus(ZeroOrOneConstants.ONE);
        monitorTcp.setAvgTime(avgTime);
        monitorTcp.setUpdateTime(date);
        // 更新数据库
        this.tcpService.updateById(monitorTcp);
        // 添加历史记录
        MonitorTcpHistory monitorTcpHistory = new MonitorTcpHistory();
        monitorTcpHistory.setTcpId(monitorTcp.getId());
        monitorTcpHistory.setHostnameSource(monitorTcp.getHostnameSource());
        monitorTcpHistory.setHostnameTarget(monitorTcp.getHostnameTarget());
        monitorTcpHistory.setPortTarget(monitorTcp.getPortTarget());
        monitorTcpHistory.setDescr(monitorTcp.getDescr());
        monitorTcpHistory.setStatus(monitorTcp.getStatus());
        monitorTcpHistory.setAvgTime(monitorTcp.getAvgTime());
        monitorTcpHistory.setOfflineCount(monitorTcp.getOfflineCount());
        monitorTcpHistory.setInsertTime(date);
        monitorTcpHistory.setUpdateTime(date);
        this.tcpHistoryService.save(monitorTcpHistory);
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnum  告警级别
     * @param alarmReasonEnum 告警原因
     * @param monitorTcp      TCP信息
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2022/1/12 11:33
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum, MonitorTcp monitorTcp) throws NetException, SigarException {
        StringBuilder builder = new StringBuilder();
        builder.append("源主机：").append(monitorTcp.getHostnameSource())
                .append("，<br>目标主机：").append(monitorTcp.getHostnameTarget())
                .append("，<br>目标端口：").append(monitorTcp.getPortTarget());
        if (StringUtils.isNotBlank(monitorTcp.getDescr())) {
            builder.append("，<br>描述：").append(monitorTcp.getDescr());
        }
        builder.append("，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                // 保证code的唯一性
                .code(Md5Utils.encrypt32(MonitorTypeEnums.TCP4SERVICE.name() + monitorTcp.getHostnameSource() + monitorTcp.getHostnameTarget() + monitorTcp.getPortTarget()))
                .title(title)
                .msg(builder.toString())
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(MonitorTypeEnums.TCP4SERVICE)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
