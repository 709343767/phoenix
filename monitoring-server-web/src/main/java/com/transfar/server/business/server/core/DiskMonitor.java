package com.transfar.server.business.server.core;

import com.transfar.common.constant.AlarmLevelEnums;
import com.transfar.common.domain.Alarm;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.inf.ICallback;
import com.transfar.server.business.server.domain.Disk;
import com.transfar.server.business.server.service.IAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务器磁盘监控器，更新磁盘状态，发送告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/30 15:06
 */
@Component
@Slf4j
public class DiskMonitor implements ICallback, DisposableBean {

    /**
     * 服务器磁盘信息池
     */
    @Autowired
    private DiskPool diskPool;

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * 线程池
     */
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {
        AtomicInteger atomic = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "monitoring-disk-pool-thread-" + this.atomic.getAndIncrement());
        }
    });

    /**
     * <p>
     * 监控磁盘使用率，在磁盘过载时发送告警信息
     * </p>
     *
     * @param obj 回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 15:35
     */
    @Override
    public void event(Object... obj) {
        this.cachedThreadPool.execute(() -> {
            String key = String.valueOf(obj[0]);
            Disk disk = this.diskPool.get(key);
            ConcurrentHashMap<String, Disk.Subregion> subregionConcurrentHashMap = disk.getSubregionConcurrentHashMap();
            for (Map.Entry<String, Disk.Subregion> entry : subregionConcurrentHashMap.entrySet()) {
                Disk.Subregion subregion = entry.getValue();
                // 分区的盘符资源利用率
                double usePercent = subregion.getUsePercent();
                // 利用率大于90%
                if (usePercent > 90) {
                    // 是否已经发送过告警
                    boolean isAlarm = subregion.isAlarm();
                    subregion.setOverLoad(true);
                    if (!isAlarm) {
                        // 发送告警
                        this.sendAlarmInfo("磁盘分区过载", AlarmLevelEnums.WARN, disk.getIp(), subregion);
                        subregion.setAlarm(true);
                    }
                }
                // 利用率正常
                else {
                    subregion.setAlarm(false);
                    subregion.setOverLoad(false);
                }
            }
            log.info("当前服务器磁盘信息池大小：{}，过载：{}，详细信息：{}",//
                    this.diskPool.size(),//
                    this.diskPool.values().stream().map(dsk -> {
                        ConcurrentHashMap<String, Disk.Subregion> subregionMap = dsk.getSubregionConcurrentHashMap();//
                        return subregionMap.entrySet().stream().filter(o -> o.getValue().isOverLoad())//
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    }).count(),//
                    this.diskPool.toJsonString());
        });
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param ip              IP地址
     * @param subregion       磁盘分区
     * @author 皮锋
     * @custom.date 2020/3/25 14:46
     */
    private synchronized void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, String ip, Disk.Subregion subregion) {
        this.cachedThreadPool.execute(() -> {
            String msg = "IP地址：" + ip + "，磁盘分区：" + subregion.getDevName() + "，磁盘分区使用率：" + subregion.getUsePercent();
            Alarm alarm = Alarm.builder()//
                    .title(title)//
                    .msg(msg)//
                    .alarmLevel(alarmLevelEnums)//
                    .build();
            AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
            this.alarmService.dealAlarmPackage(alarmPackage);
        });
    }

    /**
     * <p>
     * 在spring容器销毁时关闭线程池
     * </p>
     * 关闭线程池：monitoring-disk-pool-thread
     *
     * @author 皮锋
     * @custom.date 2020/3/26 16:33
     */
    @Override
    public void destroy() {
        if (!this.cachedThreadPool.isShutdown()) {
            this.cachedThreadPool.shutdown();
            log.info("线程池“monitoring-disk-pool-thread”已经关闭！");
        }
    }

}
