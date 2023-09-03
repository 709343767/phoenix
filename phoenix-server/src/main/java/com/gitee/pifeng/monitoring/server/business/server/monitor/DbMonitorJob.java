package com.gitee.pifeng.monitoring.server.business.server.monitor;

import cn.hutool.db.DbUtil;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.handler.NumberHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.gitee.pifeng.monitoring.common.constant.DbEnums;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.sql.MySql;
import com.gitee.pifeng.monitoring.common.constant.sql.Oracle;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPoolShutdownHelper;
import com.gitee.pifeng.monitoring.common.util.CollectionUtils;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.common.util.server.ProcessorsUtils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.ServerPackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorDb;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IDbService;
import com.gitee.pifeng.monitoring.server.util.db.MongoUtils;
import com.gitee.pifeng.monitoring.server.util.db.RedisUtils;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

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
@Order(4)
public class DbMonitorJob extends QuartzJobBean {

    /**
     * 监控配置属性加载器
     */
    @Autowired
    private MonitoringConfigPropertiesLoader monitoringConfigPropertiesLoader;

    /**
     * 服务端包构造器
     */
    @Autowired
    private ServerPackageConstructor serverPackageConstructor;

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * 数据库表服务接口
     */
    @Autowired
    private IDbService dbService;

    /**
     * 线程池
     */
    private final ScheduledExecutorService seService = new ScheduledThreadPoolExecutor(
            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("phoenix-server-db-monitor-pool-thread-%d")
                    // 设置为守护线程
                    .daemon(true)
                    .build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 在程序关闭前优雅关闭线程池
     *
     * @author 皮锋
     * @custom.date 2023/6/4 22:00
     */
    @PreDestroy
    public void shutdownThreadPoolGracefully() {
        new ThreadPoolShutdownHelper().shutdownGracefully(this.seService, "phoenix-server-db-monitor-pool-thread");
    }

    /**
     * 扫描数据库“MONITOR_DB”表中的所有数据库信息，实时更新数据库状态，发送告警。
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2020/11/23 22:00
     */
    @Override
    protected void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {
        // 是否监控数据库
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getDbProperties().isEnable();
        if (!isEnable) {
            return;
        }
        synchronized (DbMonitorJob.class) {
            try {
                // 查询数据库中的所有数据库信息
                List<MonitorDb> monitorDbs = this.dbService.list();
                // 打乱
                Collections.shuffle(monitorDbs);
                // 按每个list大小为10拆分成多个list
                List<List<MonitorDb>> subMonitorDbLists = CollectionUtils.split(monitorDbs, 10);
                for (List<MonitorDb> subMonitorDbs : subMonitorDbLists) {
                    // 使用多线程，加快处理速度
                    this.seService.execute(() -> {
                        for (MonitorDb monitorDb : subMonitorDbs) {
                            String dbType = monitorDb.getDbType();
                            // 关系型数据库
                            if (StringUtils.equalsIgnoreCase(DbEnums.MySQL.name(), dbType)
                                    || StringUtils.equalsIgnoreCase(DbEnums.Oracle.name(), dbType)) {
                                // 处理关系型数据库
                                this.dealRelationalDb(monitorDb);
                            }
                            // Redis数据库
                            if (StringUtils.equalsIgnoreCase(DbEnums.Redis.name(), dbType)) {
                                // 处理Redis数据库
                                this.dealRedisDb(monitorDb);
                            }
                            // mongo数据库
                            if (StringUtils.equalsIgnoreCase(DbEnums.Mongo.name(), dbType)) {
                                // 处理MongoDB
                                this.dealMongoDb(monitorDb);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                log.error("定时扫描数据库“MONITOR_DB”表中的所有数据库信息异常！", e);
            }
        }
    }

    /**
     * <p>
     * 处理MongoDB
     * </p>
     *
     * @param monitorDb 数据库信息
     * @author 皮锋
     * @custom.date 2022/1/19 16:27
     */
    private void dealMongoDb(MonitorDb monitorDb) {
        MongoClient mongoClient = null;
        try {
            String url = monitorDb.getUrl();
            // 获取 MongoClient
            mongoClient = MongoUtils.getClient(url);
            // Mongo数据库是否可连接
            boolean isConnect = MongoUtils.isConnect(mongoClient);
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
        } catch (Exception e) {
            // 处理数据库异常
            this.disconnected(monitorDb);
            log.error("执行数据库监控异常！", e);
        } finally {
            MongoUtils.close(mongoClient);
        }
    }

    /**
     * <p>
     * 处理Redis数据库
     * </p>
     *
     * @param monitorDb 数据库信息
     * @author 皮锋
     * @custom.date 2021/10/16 14:00
     */
    private void dealRedisDb(MonitorDb monitorDb) {
        Jedis jedis = null;
        try {
            String url = monitorDb.getUrl();
            String[] address = StringUtils.split(url, ":");
            // 主机
            String host = address[0];
            // 端口
            int port = Integer.parseInt(address[1]);
            // 密码
            String password = monitorDb.getPassword();
            // 获取 Jedis
            jedis = RedisUtils.getJedis(host, port, password);
            // Redis数据库是否可连接
            boolean isConnect = RedisUtils.isConnect(jedis);
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
        } catch (Exception e) {
            // 处理数据库异常
            this.disconnected(monitorDb);
            log.error("执行数据库监控异常！", e);
        } finally {
            RedisUtils.close(jedis);
        }
    }

    /**
     * <p>
     * 处理关系型数据库
     * </p>
     *
     * @param monitorDb 数据库信息
     * @author 皮锋
     * @custom.date 2021/10/16 13:55
     */
    private void dealRelationalDb(MonitorDb monitorDb) {
        // 关系型数据库连接
        Connection connection = this.getRelationalDbConnection(monitorDb);
        try {
            // 关系型数据库是否可连接
            boolean isConnect = this.isRelationalDbConnect(connection, monitorDb);
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
        } catch (Exception e) {
            // 处理数据库异常
            this.disconnected(monitorDb);
            log.error("执行数据库监控异常！", e);
        } finally {
            DbUtil.close(connection);
        }
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
        try {
            this.sendAlarmInfo("数据库连接异常", AlarmLevelEnums.FATAL, AlarmReasonEnums.NORMAL_2_ABNORMAL, monitorDb);
        } catch (Exception e) {
            log.error("数据库告警异常！", e);
        }
        // 原本是在线或者未知
        String isOnline = monitorDb.getIsOnline();
        if (StringUtils.equalsIgnoreCase(isOnline, ZeroOrOneConstants.ONE) || StringUtils.isBlank(isOnline)) {
            // 离线次数 +1
            int offlineCount = monitorDb.getOfflineCount() == null ? 0 : monitorDb.getOfflineCount();
            monitorDb.setOfflineCount(offlineCount + 1);
        }
        monitorDb.setIsOnline(ZeroOrOneConstants.ZERO);
        monitorDb.setUpdateTime(new Date());
        // 更新数据库
        this.dbService.updateById(monitorDb);
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
        try {
            if (StringUtils.isNotBlank(monitorDb.getIsOnline())) {
                this.sendAlarmInfo("数据库连接恢复", AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL, monitorDb);
            }
        } catch (Exception e) {
            log.error("数据库告警异常！", e);
        }
        monitorDb.setIsOnline(ZeroOrOneConstants.ONE);
        monitorDb.setUpdateTime(new Date());
        // 更新数据库
        this.dbService.updateById(monitorDb);
    }

    /**
     * <p>
     * 获取关系型数据库连接
     * </p>
     *
     * @param monitorDb 数据库信息
     * @return 数据库连接
     * @author 皮锋
     * @custom.date 2020/12/21 21:42
     */
    private Connection getRelationalDbConnection(MonitorDb monitorDb) {
        // url
        String url = monitorDb.getUrl();
        // 用户名
        String username = monitorDb.getUsername();
        // 密码
        String password = new String(Base64.getDecoder().decode(monitorDb.getPassword()), StandardCharsets.UTF_8);
        // 数据源
        try (SimpleDataSource ds = new SimpleDataSource(url, username, password)) {
            return ds.getConnection();
        } catch (Exception e) {
            log.error("与数据库建立连接异常！", e);
            return null;
        }
    }

    /**
     * <p>
     * 关系型数据库是否可连接
     * </p>
     *
     * @param connection 数据库连接
     * @param monitorDb  数据库表
     * @return 是 或者 否
     * @author 皮锋
     * @custom.date 2020/12/20 16:07
     */
    private boolean isRelationalDbConnect(Connection connection, MonitorDb monitorDb) {
        // 数据库连接为空
        if (connection == null) {
            return false;
        }
        try {
            String dbType = monitorDb.getDbType();
            // mysql
            if (StringUtils.equalsIgnoreCase(dbType, DbType.MYSQL.getDb())) {
                SqlExecutor.query(connection, MySql.CHECK_CONN, new NumberHandler());
            }
            // oracle
            if (StringUtils.equalsIgnoreCase(dbType, DbType.ORACLE.getDb())) {
                SqlExecutor.query(connection, Oracle.CHECK_CONN, new NumberHandler());
            }
            return true;
        } catch (Exception e) {
            log.error("检查连接异常！", e);
            return false;
        }
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnum  告警级别
     * @param alarmReasonEnum 告警原因
     * @param monitorDb       数据库信息
     * @throws NetException 获取数据库信息异常
     * @author 皮锋
     * @custom.date 2020/12/20 21:25
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum, MonitorDb monitorDb) throws NetException {
        StringBuilder builder = new StringBuilder();
        // 数据库类型
        String dbType = monitorDb.getDbType();
        builder.append("连接名：").append(monitorDb.getConnName());
        if (StringUtils.equalsIgnoreCase(DbEnums.Redis.name(), dbType)) {
            builder.append("，<br>地址：");
        } else {
            builder.append("，<br>URL：");
        }
        builder.append(monitorDb.getUrl())
                .append("，<br>类型：").append(dbType);
        if (StringUtils.isNotBlank(monitorDb.getDbDesc())) {
            builder.append("，<br>描述：").append(monitorDb.getDbDesc());
        }
        builder.append("，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                // 保证code的唯一性
                .code(Md5Utils.encrypt32(monitorDb.getUrl() + monitorDb.getUsername()))
                .title(title)
                .msg(builder.toString())
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(MonitorTypeEnums.DATABASE)
                .build();
        AlarmPackage alarmPackage = this.serverPackageConstructor.structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
