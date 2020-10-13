package com.imby.plug.thread;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.common.exception.NetException;
import com.imby.plug.constant.UrlConstants;
import com.imby.plug.core.PackageConstructor;
import com.imby.plug.core.Sender;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;

import java.io.IOException;

/**
 * <p>
 * 心跳线程
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午2:50:46
 */
@Slf4j
public class HeartbeatThread implements Runnable {

    /**
     * <p>
     * 发送心跳包
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/4/9 17:32
     */
    @Override
    public void run() {
        try {
            // 构建心跳数据包
            HeartbeatPackage heartbeatPackage = new PackageConstructor().structureHeartbeatPackage();
            // 计时器
            TimeInterval timer = DateUtil.timer();
            // 发送请求
            String result = Sender.send(UrlConstants.HEARTBEAT_URL, heartbeatPackage.toJsonString());
            // 时间差（毫秒）
            String betweenDay = timer.intervalPretty();
            log.debug("发送心跳包耗时：{}", betweenDay);
            log.debug("心跳包响应消息：{}", result);
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
