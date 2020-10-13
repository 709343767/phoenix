package com.imby.plug.thread;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.imby.common.dto.JvmPackage;
import com.imby.common.exception.NetException;
import com.imby.plug.constant.UrlConstants;
import com.imby.plug.core.PackageConstructor;
import com.imby.plug.core.Sender;
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
     * 发送Java虚拟机信息包
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 21:25
     */
    @Override
    public void run() {
        try {
            JvmPackage jvmPackage = new PackageConstructor().structureJvmPackage();
            // 计时器
            TimeInterval timer = DateUtil.timer();
            // 发送请求
            String result = Sender.send(UrlConstants.JVM_URL, jvmPackage.toJsonString());
            // 时间差（毫秒）
            String betweenDay = timer.intervalPretty();
            log.debug("发送Java虚拟机信息包耗时：{}", betweenDay);
            log.debug("Java虚拟机信息包响应消息：{}", result);
        } catch (IOException e) {
            log.error("IO异常！", e);
        } catch (NetException e) {
            log.error("获取网络信息异常！", e);
        } catch (SigarException e) {
            log.error("Sigar异常！", e);
        } catch (Exception e) {
            log.error("其它异常！", e);
        }
    }

}
