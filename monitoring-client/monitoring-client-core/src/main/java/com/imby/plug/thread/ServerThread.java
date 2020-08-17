package com.imby.plug.thread;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.imby.common.dto.ServerPackage;
import com.imby.plug.constant.UrlConstants;
import com.imby.plug.core.PackageConstructor;
import com.imby.plug.core.Sender;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.hyperic.sigar.SigarException;

import java.io.IOException;
import java.util.Date;

/**
 * <p>
 * 发送服务器信息线程
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午4:48:48
 */
@Slf4j
public class ServerThread implements Runnable {

    /**
     * <p>
     * 发送服务器信息包
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/4/9 17:30
     */
    @Override
    public void run() {
        try {
            // 构建服务器数据包
            ServerPackage serverPackage = new PackageConstructor().structureServerPackage();
            // 开始时间
            Date beginDate = new Date();
            // 发送请求
            String result = Sender.send(UrlConstants.SERVER_URL, serverPackage.toJsonString());
            // 结束时间
            Date endDate = new Date();
            // 时间差（毫秒）
            long betweenDay = DateUtil.between(beginDate, endDate, DateUnit.MS);
            log.debug("服务器信息包响应消息：{}", result);
            log.debug("发送服务器信息包耗时：{} {}", betweenDay, "ms");
        } catch (ClientProtocolException e) {
            log.error("客户端协议异常！", e);
        } catch (IOException e) {
            log.error("IO异常！", e);
        } catch (SigarException e) {
            log.error("Sigar异常！", e);
        }
    }

}
