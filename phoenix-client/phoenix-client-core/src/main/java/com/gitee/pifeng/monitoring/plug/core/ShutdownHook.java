package com.gitee.pifeng.monitoring.plug.core;

import com.gitee.pifeng.monitoring.common.constant.EndpointTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPoolManager;
import com.gitee.pifeng.monitoring.plug.thread.OfflineThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 关闭钩子
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/5/31 11:34
 */
@Slf4j
public class ShutdownHook {

    /**
     * <p>
     * 添加关闭钩子
     * </p>
     *
     * @author 皮锋
     * @custom.date 2023/5/31 11:35
     */
    public static void addShutdownHook() {
        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // 第一步：如果不是服务端，jvm关闭前先发送下线数据包
                String instanceEndpoint = ConfigLoader.getMonitoringProperties().getInstance().getEndpoint();
                if (!StringUtils.equalsIgnoreCase(instanceEndpoint, EndpointTypeEnums.SERVER.getNameEn())) {
                    Result result = new OfflineThread().call();
                    log.info("下线数据包发送结果：{}", result.toJsonString());
                }
                // 第二步：释放资源
                // 1.优雅地关闭所有需要关闭的线程池并且取消注册
                ThreadPoolManager.shutdownAllGracefullyAndUnregister();
                // 2.关闭HTTP连接池释放掉连接
                EnumPoolingHttpClient.getInstance().close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }));
    }

}
