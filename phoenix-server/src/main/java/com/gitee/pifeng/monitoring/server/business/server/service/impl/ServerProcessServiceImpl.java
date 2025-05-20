package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.ProcessDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerProcessDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerProcess;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerProcessService;
import com.google.common.collect.Lists;
import org.springframework.aop.framework.AopContext;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务器进程信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 14:11
 */
@Service
public class ServerProcessServiceImpl extends ServiceImpl<IMonitorServerProcessDao, MonitorServerProcess> implements IServerProcessService {

    /**
     * <p>
     * 把服务器进程信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/9/15 14:49
     */
    @Retryable
    @Override
    public void operateServerProcess(ServerPackage serverPackage) {
        ProcessDomain processDomain = serverPackage.getServer().getProcessDomain();
        if (processDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            List<ProcessDomain.ProcessInfoDomain> processInfoList = processDomain.getProcessInfoList();
            // 先删除此服务器对应的进程信息
            LambdaUpdateWrapper<MonitorServerProcess> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorServerProcess::getIp, ip);
            this.remove(lambdaUpdateWrapper);
            // 循环所有进程信息
            List<MonitorServerProcess> insertMonitorServerProcesses = Lists.newArrayList();
            for (ProcessDomain.ProcessInfoDomain processInfo : processInfoList) {
                MonitorServerProcess monitorServerProcess = MonitorServerProcess.builder().build();
                monitorServerProcess.setIp(ip);
                monitorServerProcess.setProcessId(processInfo.getProcessId());
                monitorServerProcess.setName(processInfo.getName());
                monitorServerProcess.setPath(processInfo.getPath());
                monitorServerProcess.setCommandLine(processInfo.getCommandLine());
                monitorServerProcess.setCurrentWorkingDirectory(processInfo.getCurrentWorkingDirectory());
                monitorServerProcess.setUser(processInfo.getUser());
                monitorServerProcess.setState(processInfo.getState());
                monitorServerProcess.setUpTime(processInfo.getUpTime());
                monitorServerProcess.setStartTime(processInfo.getStartTime());
                monitorServerProcess.setCpuLoadCumulative(processInfo.getCpuLoadCumulative());
                monitorServerProcess.setBitness(processInfo.getBitness());
                monitorServerProcess.setMemorySize(processInfo.getMemorySize());
                monitorServerProcess.setPorts(processInfo.getPorts());
                monitorServerProcess.setInsertTime(currentTime);
                monitorServerProcess.setUpdateTime(currentTime);
                insertMonitorServerProcesses.add(monitorServerProcess);
            }
            ((IServerProcessService) AopContext.currentProxy()).saveBatch(insertMonitorServerProcesses);
        }
    }

}
