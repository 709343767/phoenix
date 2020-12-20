package com.gitee.pifeng.server.business.server.monitor;

import cn.hutool.db.ds.simple.SimpleDataSource;
import com.gitee.pifeng.common.constant.AlarmLevelEnums;
import com.gitee.pifeng.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.common.domain.Alarm;
import com.gitee.pifeng.common.dto.AlarmPackage;
import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.common.threadpool.ThreadPool;
import com.gitee.pifeng.common.util.DateTimeUtils;
import com.gitee.pifeng.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.server.business.server.entity.MonitorDb;
import com.gitee.pifeng.server.business.server.service.IAlarmService;
import com.gitee.pifeng.server.business.server.service.IMonitorDbService;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 在项目启动后，定时扫描数据库“MONITOR_DB”表中的所有数据库信息，更新数据库状态，发送告警。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/20 13:51
 */
@Slf4j
@Component
@Order(3)
public class DbMonitorTask implements CommandLineRunner {

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * 数据库表服务接口
     */
    @Autowired
    private IMonitorDbService monitorDbService;

    /**
     * 项目启动完成后延迟10秒钟启动定时任务，扫描数据库“MONITOR_DB”表中的所有数据库信息，实时更新数据库状态，发送告警，
     * 然后在一次执行结束和下一次执行开始之间延迟5分钟。
     *
     * @param args 传入的主方法参数
     * @author 皮锋
     * @custom.date 2020/11/23 22:00
     */
    @Override
    public void run(String... args) {
        ThreadPool.COMMON_IO_INTENSIVE_SCHEDULED_THREAD_POOL.scheduleWithFixedDelay(() -> {
            // 是否监控数据库
            boolean isEnable = MonitoringConfigPropertiesLoader.getMonitoringProperties().getDbProperties().isEnable();
            if (isEnable) {
                try {
                    // 查询数据库中的所有数据库信息
                    List<MonitorDb> monitorDbs = this.monitorDbService.list();
                    for (MonitorDb monitorDb : monitorDbs) {
                        // 数据库是否可连接
                        boolean isConnect = this.isConnect(monitorDb);
                        // 连接正常
                        if (isConnect) {
                            // 处理数据库正常
                            this.connected(monitorDb);
                        }
                        // 连接异常
                        else {
                            // 处理数据库异常
                            this.disconnected(monitorDb);
                        }
                    }
                } catch (Exception e) {
                    log.error("定时扫描数据库“MONITOR_DB”表中的所有数据库信息异常！", e);
                }
            }
        }, 10, 300, TimeUnit.SECONDS);
    }

    /**
     * <p>
     * 处理数据库异常
     * </p>
     *
     * @param monitorDb 数据库信息
     * @author 皮锋
     * @custom.date 2020/12/20 22:20
     */
    private void disconnected(MonitorDb monitorDb) {
        // 是否已经断连告警（0：否，1：是）
        boolean isAlarm = StringUtils.equals(monitorDb.getIsAlarm(), ZeroOrOneConstants.ONE);
        // 没有发送断连告警
        if (!isAlarm) {
            try {
                this.sendAlarmInfo("数据库连接异常", AlarmLevelEnums.FATAL, monitorDb);
            } catch (Exception e) {
                log.error("数据库告警异常！", e);
            }
        }
        monitorDb.setIsOnline(ZeroOrOneConstants.ZERO);
        monitorDb.setIsAlarm(ZeroOrOneConstants.ONE);
        monitorDb.setUpdateTime(new Date());
        // 更新数据库
        this.monitorDbService.updateMonitorDb(monitorDb);
    }

    /**
     * <p>
     * 处理数据库正常
     * </p>
     *
     * @param monitorDb 数据库信息
     * @author 皮锋
     * @custom.date 2020/12/20 22:12
     */
    private void connected(MonitorDb monitorDb) {
        // 是否已经断连告警（0：否，1：是）
        boolean isAlarm = StringUtils.equals(monitorDb.getIsAlarm(), ZeroOrOneConstants.ONE);
        // 已经断连告警了
        if (isAlarm) {
            try {
                this.sendAlarmInfo("数据库恢复连接", AlarmLevelEnums.INFO, monitorDb);
            } catch (Exception e) {
                log.error("数据库告警异常！", e);
            }
        }
        monitorDb.setIsOnline(ZeroOrOneConstants.ONE);
        monitorDb.setIsAlarm(ZeroOrOneConstants.ZERO);
        monitorDb.setUpdateTime(new Date());
        // 更新数据库
        this.monitorDbService.updateMonitorDb(monitorDb);
    }

    /**
     * <p>
     * 数据库是否可连接
     * </p>
     *
     * @param monitorDb 数据库表
     * @return 是 或者 否
     * @author 皮锋
     * @custom.date 2020/12/20 16:07
     */
    private boolean isConnect(MonitorDb monitorDb) {
        // url
        String url = monitorDb.getUrl();
        // 用户名
        String username = monitorDb.getUsername();
        // 密码
        String password = new String(Base64.getDecoder().decode(monitorDb.getPassword()), StandardCharsets.UTF_8);
        try {
            // 数据源
            @Cleanup
            SimpleDataSource ds = new SimpleDataSource(url, username, password);
            @Cleanup
            Connection connection = ds.getConnection();
            log.info("Catalog：{}", connection.getCatalog());
            return true;
        } catch (Exception e) {
            log.error("与数据库建立连接异常！", e);
            return false;
        }
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param monitorDb       数据库信息
     * @throws NetException   获取数据库信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/12/20 21:25
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, MonitorDb monitorDb) throws NetException, SigarException {
        StringBuilder builder = new StringBuilder();
        builder.append("url：").append(monitorDb.getUrl()).append("，<br>数据库类型：").append(monitorDb.getDbType());
        if (StringUtils.isNotBlank(monitorDb.getDbDesc())) {
            builder.append("，<br>描述：").append(monitorDb.getDbDesc());
        }
        builder.append("，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                .title(title)
                .msg(builder.toString())
                .alarmLevel(alarmLevelEnums)
                .monitorType(MonitorTypeEnums.DATABASE)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
