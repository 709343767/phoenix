package com.gitee.pifeng.monitoring.server.business.server.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * MySQL实现的分布式锁
 * </p>
 * 适用场景：低频、关键路径的互斥操作（如告警去重、配置更新）<br>
 * 🚫 不适用场景：高频秒级并发锁（如秒杀）<br>
 * 🔐 锁安全性：依赖 MySQL 唯一索引 + 自动过期，可防死锁<br>
 * 🧹 过期清理：后台每 10 秒自动清理 + 获取时概率清理<br>
 * ⏱️ 时间精度：毫秒级（要求数据库字段为 DATETIME(3)）<br>
 *
 * @author 皮锋
 * @custom.date 2025/11/6 16:47
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class MysqlDistributedLock extends QuartzJobBean {

    /**
     * 锁名称（lockKey）的最大长度，单位：字符。
     * 与数据库字段 `MONITOR_DISTRIBUTED_LOCK.LOCK_KEY` 的 VARCHAR(128) 保持一致，
     * 防止因超长导致插入失败或截断。
     */
    private static final int MAX_LOCK_KEY_LENGTH = 128;

    /**
     * 锁持有者（owner）的最大长度，单位：字符。
     * 与数据库字段 `MONITOR_DISTRIBUTED_LOCK.OWNER` 的 VARCHAR(32) 保持一致，
     * 建议使用格式如 "IP:PORT" 或 "应用实例ID"，确保全局唯一且简洁。
     */
    private static final int MAX_OWNER_LENGTH = 32;

    /**
     * 毫秒级时间格式化器
     */
    private static final DateTimeFormatter MILLIS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 使用 Spring 管理的 DataSource
     */
    @Autowired
    private DataSource dataSource;

    /**
     * <p>
     * 尝试获取锁（带自动过期）
     * </p>
     *
     * @param lockKey       锁名称
     * @param owner         锁持有者，建议全局唯一
     * @param expireSeconds 过期时间（秒），建议 ≥ 5
     * @param waitSeconds   等待时间（秒）
     * @return true：表示获得锁成功；false：表示超时未获得
     * @author 皮锋
     * @custom.date 2025/11/6 17:13
     */
    @SuppressWarnings("BusyWait")
    public boolean tryLock(String lockKey, String owner, int expireSeconds, int waitSeconds) {
        // -------------------基于：指数退避 + 动态休眠-------------------//
        // -----------初期快速重试（锁刚释放就能立即抢到）------------------//
        // -----------后期降低频率（减少 DB 压力）------------------------//
        // -----------避免固定间隔的“对齐效应”（多个线程不会同时醒来查库）----//
        // 超时时间（纳秒）
        long deadlineNanos = System.nanoTime() + waitSeconds * 1_000_000_000L;
        // 初始等待时间（纳秒）
        long delayNanos = 50_000_000L;
        // 最大等待间隔（纳秒）
        final long maxDelayNanos = 500_000_000L;

        while (System.nanoTime() < deadlineNanos) {
            if (this.acquireLock(lockKey, owner, expireSeconds)) {
                return true;
            }
            // 计算剩余时间，避免 sleep 超时
            long remainingNanos = deadlineNanos - System.nanoTime();
            if (remainingNanos <= 0) {
                break;
            }
            // ±50% 扰动
            long jitter = ThreadLocalRandom.current().nextLong(0, delayNanos / 2);
            // 取 min(当前delay+jitter, 剩余时间, maxDelay)
            long sleepNanos = Math.min(delayNanos + jitter, Math.min(remainingNanos, maxDelayNanos));
            try {
                // Thread.sleep 接受毫秒 + 纳秒
                long millis = sleepNanos / 1_000_000L;
                int nanos = (int) (sleepNanos % 1_000_000L);
                // 注意：此处为分布式锁的标准重试模式，非真正 busy-wait。
                // 因 MySQL 无法提供锁释放通知，必须通过带退避的轮询实现。
                // 已通过指数退避 + 最大间隔 + 超时控制避免 DB 压力。
                Thread.sleep(millis, nanos);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
            // 指数退避：50 → 100 → 200 → 400 → 500（上限）
            delayNanos = Math.min(delayNanos * 2, maxDelayNanos);
        }
        return false;
    }

    /**
     * <p>
     * 释放锁（只有持有者才能释放）
     * </p>
     *
     * @param lockKey 锁名称
     * @param owner   锁持有者
     * @return true：表示释放锁成功；false：表示释放锁失败
     * @author 皮锋
     * @custom.date 2025/11/6 17:12
     */
    public boolean releaseLock(String lockKey, String owner) {
        String sql = "DELETE FROM MONITOR_DISTRIBUTED_LOCK WHERE LOCK_KEY = ? AND OWNER = ?";
        try (Connection conn = this.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lockKey);
            ps.setString(2, owner);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            if (log.isDebugEnabled()) {
                log.debug("释放锁失败：key={}, owner={}", lockKey, owner, e);
            }
            return false;
        }
    }

    /**
     * <p>
     * 后台定时清理所有已过期的锁（可定期调用，或每次获取前清理）
     * </p>
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2025/11/6 17:10
     * @see #execute
     */
    @Override
    protected void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {
        String sql = "DELETE FROM MONITOR_DISTRIBUTED_LOCK WHERE EXPIRE_TIME <= NOW(3) LIMIT 1000";
        try (Connection conn = this.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int deleted = ps.executeUpdate();
            if (deleted > 0 && log.isDebugEnabled()) {
                log.debug("清理 {} 个过期锁！", deleted);
            }
        } catch (SQLException e) {
            if (log.isDebugEnabled()) {
                log.debug("清理过期锁失败：{}", e.getMessage(), e);
            }
        }
    }

    /**
     * <p>
     * 抢占锁
     * </p>
     * 尝试去拿到一把锁，如果成功，就拥有了对某段资源的独占使用权。
     *
     * @param lockKey       锁名称
     * @param owner         锁持有者
     * @param expireSeconds 过期时间
     * @return true：表示抢占锁成功；false：表示抢占锁失败
     * @author 皮锋
     * @custom.date 2025/11/6 16:59
     */
    private boolean acquireLock(String lockKey, String owner, int expireSeconds) {
        if (StringUtils.isEmpty(lockKey)) {
            throw new IllegalArgumentException("lockKey 不能为空！");
        }
        if (lockKey.length() > MAX_LOCK_KEY_LENGTH) {
            throw new IllegalArgumentException("lockKey 太长！");
        }
        if (StringUtils.isEmpty(owner)) {
            throw new IllegalArgumentException("owner 不能为空！");
        }
        if (owner.length() > MAX_OWNER_LENGTH) {
            throw new IllegalArgumentException("owner 太长！");
        }
        // 概率清理（20%的请求触发清理），避免高频请求压垮 DB
        // if (ThreadLocalRandom.current().nextDouble() < 0.2) {
        //     this.cleanExpiredLocks();
        // }
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(expireSeconds)
                // 添加纳秒抖动
                .plusNanos(ThreadLocalRandom.current().nextInt(0, 1_000_000));
        String expireTimeStr = expireTime.format(MILLIS_FORMATTER);
        // 关键 SQL：插入或抢占过期锁
        String sql = "INSERT INTO MONITOR_DISTRIBUTED_LOCK (LOCK_KEY, OWNER, EXPIRE_TIME) "
                + "VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE OWNER = IF(EXPIRE_TIME <= NOW(3), VALUES(OWNER), OWNER), "
                + "EXPIRE_TIME = IF(EXPIRE_TIME <= NOW(3), VALUES(EXPIRE_TIME), EXPIRE_TIME)";
        try (Connection conn = this.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lockKey);
            ps.setString(2, owner);
            ps.setString(3, expireTimeStr);
            int rows = ps.executeUpdate();
            // 插入成功（1行） 或 更新成功（2行）都算获取锁成功
            // 即使更新0行（极罕见），也视为失败更安全
            return rows > 0;
        } catch (Exception e) {
            // 唯一约束冲突 or 其他异常 → 获取失败
            if (log.isDebugEnabled()) {
                log.debug("抢占锁失败：key={}, owner={}", lockKey, owner, e);
            }
            return false;
        }
    }

    /**
     * <p>
     * 获取一个 auto-commit=true 的独立数据库连接（不参与 Spring 事务）
     * </p>
     *
     * @return {@link Connection}
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2025/11/13 08:39
     */
    private Connection getConnection() throws SQLException {
        // 方式1：直接从 DataSource 获取（推荐）
        Connection conn = this.dataSource.getConnection();
        // 确保自动提交
        conn.setAutoCommit(true);
        return conn;
        // 不要使用 DataSourceUtils.getConnection(dataSource)，
        // 因为它会绑定到当前 Spring 事务！
    }

}