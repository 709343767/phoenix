package com.gitee.pifeng.monitoring.server.business.server.monitor;

import com.gitee.pifeng.monitoring.server.business.server.service.*;
import com.gitee.pifeng.monitoring.server.constant.ComponentOrderConstants;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * 在项目启动后，定时清理数据库历史记录表数据。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/12/9 20:23
 */
@Slf4j
@Component
@Order(ComponentOrderConstants.OTHER + 1)
@DisallowConcurrentExecution
public class ClearHistoryDataJob extends QuartzJobBean {

    /**
     * java虚拟机信息服务层接口
     */
    @Autowired
    private IJvmService jvmService;

    /**
     * 服务器信息服务层接口
     */
    @Autowired
    private IServerService serverService;

    /**
     * 网络信息历史记录服务接口
     */
    @Autowired
    private INetHistoryService netHistoryService;

    /**
     * TCP信息历史记录服务类
     */
    @Autowired
    private ITcpHistoryService tcpHistoryService;

    /**
     * HTTP信息历史记录服务接口
     */
    @Autowired
    private IHttpHistoryService httpHistoryService;

    /**
     * 扫描数据库所有历史记录表，清理一星期以前的历史记录。
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2020/12/09 20:28
     */
    @Override
    protected void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {
        try {
            // 清理一星期以前的数据
            Date historyTime = new DateTime().plusWeeks(-1).toDate();
            int clearJvmHistoryDataNum = this.jvmService.clearHistoryData(historyTime);
            log.info("清理JVM历史数据：{} 条！", clearJvmHistoryDataNum);
            int clearServerHistoryDataNum = this.serverService.clearHistoryData(historyTime);
            log.info("清理服务器历史数据：{} 条！", clearServerHistoryDataNum);
            int clearNetHistoryDataNum = this.netHistoryService.clearHistoryData(historyTime);
            log.info("清理网络历史数据：{} 条！", clearNetHistoryDataNum);
            int clearTcpHistoryDataNum = this.tcpHistoryService.clearHistoryData(historyTime);
            log.info("清理TCP历史数据：{} 条！", clearTcpHistoryDataNum);
            int clearHttpHistoryDataNum = this.httpHistoryService.clearHistoryData(historyTime);
            log.info("清理HTTP历史数据：{} 条！", clearHttpHistoryDataNum);
        } catch (Exception e) {
            log.error("清理历史数据出错！", e);
        }
    }

}
