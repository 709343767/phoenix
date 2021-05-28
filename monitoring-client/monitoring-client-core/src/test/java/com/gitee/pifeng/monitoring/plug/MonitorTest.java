package com.gitee.pifeng.monitoring.plug;

import com.gitee.pifeng.monitoring.common.constant.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.ThreadTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.google.common.base.Charsets;
import org.junit.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 测试监控客户端入口类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/17 9:53
 */
public class MonitorTest {

    /**
     * <p>
     * 测试发送告警信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/17 9:57
     */
    @Test
    public void testSendAlarm() {
        // 开启监控
        Monitor.start();
        // 业务埋点监控：定时监控业务运行情况
        ScheduledExecutorService service = Monitor.buryingPoint(() -> {
            // 封装告警信息
            Alarm alarm = Alarm.builder().alarmLevel(AlarmLevelEnums.INFO)
                    .monitorType(MonitorTypeEnums.CUSTOM)
                    .title("测试发送告警信息")
                    .msg("测试发送告警信息")
                    .charset(Charsets.UTF_8)
                    .isTest(false)
                    .build();
            // 发送告警信息
            Result result = Monitor.sendAlarm(alarm);
            System.out.println(result.toJsonString());
        }, 5, 60, TimeUnit.SECONDS, ThreadTypeEnums.IO_INTENSIVE_THREAD);

        service.shutdown();
    }
}
