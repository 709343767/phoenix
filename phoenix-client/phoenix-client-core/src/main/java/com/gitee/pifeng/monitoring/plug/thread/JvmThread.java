package com.gitee.pifeng.monitoring.plug.thread;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.gitee.pifeng.monitoring.common.domain.Jvm;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.jvm.JvmUtils;
import com.gitee.pifeng.monitoring.plug.constant.UrlConstants;
import com.gitee.pifeng.monitoring.plug.core.PackageConstructor;
import com.gitee.pifeng.monitoring.plug.core.Sender;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;

import java.io.IOException;

/**
 * <p>
 * 发送Java虚拟机信息线程
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 21:24
 */
@Slf4j
public class JvmThread implements Runnable {

    /**
     * <p>
     * 构建+发送Java虚拟机信息包
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 21:25
     */
    @Override
    public void run() {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        try {
            // 获取Java虚拟机信息
            Jvm jvm = JvmUtils.getJvmInfo();
            // 构建Java虚拟机信息包
            JvmPackage jvmPackage = new PackageConstructor().structureJvmPackage(jvm);
            // 发送请求
            String result = Sender.send(UrlConstants.JVM_URL, jvmPackage.toJsonString());
            log.debug("Java虚拟机信息包响应消息：{}", result);
        } catch (IOException e) {
            log.error("IO异常！", e);
        } catch (NetException e) {
            log.error("获取网络信息异常！", e);
        } catch (SigarException e) {
            log.error("Sigar异常！", e);
        } catch (Exception e) {
            log.error("其它异常！", e);
        } finally {
            // 时间差（毫秒）
            String betweenDay = timer.intervalPretty();
            // 临界值
            int criticalValue = 5;
            if (timer.intervalSecond() > criticalValue) {
                log.warn("构建+发送Java虚拟机信息包耗时：{}", betweenDay);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("构建+发送Java虚拟机信息包耗时：{}", betweenDay);
                }
            }
        }
    }

}
