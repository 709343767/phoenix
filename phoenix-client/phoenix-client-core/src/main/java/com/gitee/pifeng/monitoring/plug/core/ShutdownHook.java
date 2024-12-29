package com.gitee.pifeng.monitoring.plug.core;

import com.gitee.pifeng.monitoring.common.constant.EndpointTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.threadpool.ExecutorObject;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPoolShutdownHelper;
import com.gitee.pifeng.monitoring.plug.thread.OfflineThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

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
     * @param executorObjects {@link ExecutorObject}
     * @author 皮锋
     * @custom.date 2023/5/31 11:35
     */
    public static void addShutdownHook(ExecutorObject... executorObjects) {
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
                ThreadPoolShutdownHelper threadShutdownHook = new ThreadPoolShutdownHelper();
                // 1.优雅关闭所有业务线程池
                for (ExecutorObject executorObject : executorObjects) {
                    if (executorObject != null) {
                        // 线程池名称
                        String name = executorObject.getName();
                        // 线程池
                        ScheduledExecutorService scheduledExecutorService = executorObject.getScheduledExecutorService();
                        ThreadPoolExecutor threadPoolExecutor = executorObject.getThreadPoolExecutor();
                        if (scheduledExecutorService != null) {
                            threadShutdownHook.shutdownGracefully(scheduledExecutorService, name);
                        }
                        if (threadPoolExecutor != null) {
                            threadShutdownHook.shutdownGracefully(threadPoolExecutor, name);
                        }
                    }
                }
                // 2.优雅关闭所有公共线程池
                threadShutdownHook.shutdownAllPublicThreadPoolGracefully();
                // 3.关闭HTTP连接池释放掉连接
                EnumPoolingHttpClient.getInstance().close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }));
    }

}
