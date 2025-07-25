package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.*;
import com.gitee.pifeng.monitoring.server.business.server.entity.*;
import com.gitee.pifeng.monitoring.server.business.server.service.*;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 服务器信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/23 15:23
 */
@Service
public class ServerServiceImpl extends ServiceImpl<IMonitorServerDao, MonitorServer> implements IServerService {

    /**
     * 服务器操作系统信息服务层接口
     */
    @Autowired
    private IServerOsService serverOsService;

    /**
     * 服务器内存信息服务层接口
     */
    @Autowired
    private IServerMemoryService serverMemoryService;

    /**
     * 服务器内存历史记录信息服务层接口
     */
    @Autowired
    private IServerMemoryHistoryService serverMemoryHistoryService;

    /**
     * 服务器CPU信息服务层接口
     */
    @Autowired
    private IServerCpuService serverCpuService;

    /**
     * 服务器CPU历史记录信息服务层接口
     */
    @Autowired
    private IServerCpuHistoryService serverCpuHistoryService;

    /**
     * 服务器GPU信息服务层接口
     */
    @Autowired
    private IServerGpuService serverGpuService;

    /**
     * 服务器网卡信息服务层接口
     */
    @Autowired
    private IServerNetcardService serverNetcardService;

    /**
     * 服务器网卡历史记录信息服务层接口
     */
    @Autowired
    private IServerNetcardHistoryService serverNetcardHistoryService;

    /**
     * 服务器磁盘信息服务层接口
     */
    @Autowired
    private IServerDiskService serverDiskService;

    /**
     * 服务器磁盘历史记录服务层接口
     */
    @Autowired
    private IServerDiskHistoryService serverDiskHistoryService;

    /**
     * 服务器电池信息服务层接口
     */
    @Autowired
    private IServerPowerSourcesService serverPowerSourcesService;

    /**
     * 服务器传感器信息服务层接口
     */
    @Autowired
    private IServerSensorsService serverSensorsService;

    /**
     * 服务器进程信息服务层接口
     */
    @Autowired
    private IServerProcessService serverProcessService;

    /**
     * 服务器进程历史记录信息服务层接口
     */
    @Autowired
    private IServerProcessHistoryService serverProcessHistoryService;

    /**
     * 服务器平均负载服务层接口
     */
    @Autowired
    private IServerLoadAverageService serverLoadAverageService;

    /**
     * 服务器平均负载历史记录服务层接口
     */
    @Autowired
    private IServerLoadAverageHistoryService serverLoadAverageHistoryService;

    /**
     * 服务器进程历史记录信息数据访问对象
     */
    @Autowired
    private IMonitorServerProcessHistoryDao monitorServerProcessHistoryDao;

    /**
     * 服务器CPU历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerCpuHistoryDao monitorServerCpuHistoryDao;

    /**
     * 服务器磁盘历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerDiskHistoryDao monitorServerDiskHistoryDao;

    /**
     * 服务器内存历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerMemoryHistoryDao monitorServerMemoryHistoryDao;

    /**
     * 服务器网卡历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerNetcardHistoryDao monitorServerNetcardHistoryDao;

    /**
     * 服服务器平均负载历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerLoadAverageHistoryDao monitorServerLoadAverageHistoryDao;

    /**
     * <p>
     * 处理服务器信息包
     * </p>
     * 此处不加事务，因为操作的表太多，数据太多，不加事务能提高并发性能，而且此处对数据的一致性要求并不是很高。
     *
     * @param serverPackage 服务器信息包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/3/23 15:29
     */
    //@Transactional(rollbackFor = Throwable.class)
    @Override
    public Result dealServerPackage(ServerPackage serverPackage) {
        // 把服务器信息添加或更新到数据库
        ((IServerService) AopContext.currentProxy()).operateServer(serverPackage);
        // 把服务器操作系统信息添加或更新到数据库
        this.serverOsService.operateServerOs(serverPackage);
        // 把服务器内存信息添加或更新到数据库
        this.serverMemoryService.operateServerMemory(serverPackage);
        // 把服务器内存历史记录添加到数据库
        this.serverMemoryHistoryService.operateServerMemoryHistory(serverPackage);
        // 把服务器CPU信息添加或更新到数据库
        this.serverCpuService.operateServerCpu(serverPackage);
        // 把服务器CPU历史记录添加到数据库
        this.serverCpuHistoryService.operateServerCpuHistory(serverPackage);
        // 把服务器GPU信息添加或更新到数据库
        this.serverGpuService.operateServerGpu(serverPackage);
        // 把服务器网卡信息添加或更新到数据库
        this.serverNetcardService.operateServerNetcard(serverPackage);
        // 把服务器网卡历史记录添加到数据库
        this.serverNetcardHistoryService.operateServerNetcardHistory(serverPackage);
        // 把服务器磁盘信息添加或更新到数据库
        this.serverDiskService.operateServerDisk(serverPackage);
        // 把服务器磁盘历史记录添加到数据库
        this.serverDiskHistoryService.operateServerDiskHistory(serverPackage);
        // 把服务器电池信息添加或更新到数据库
        this.serverPowerSourcesService.operateServerPowerSources(serverPackage);
        // 把服务器传感器信息添加或更新到数据库
        this.serverSensorsService.operateServerSensors(serverPackage);
        // 把服务器进程信息添加或更新到数据库
        this.serverProcessService.operateServerProcess(serverPackage);
        // 把服务器进程历史记录添加到数据库
        this.serverProcessHistoryService.operateServerProcessHistory(serverPackage);
        // 把服务器平均负载信息添加或更新到数据库
        this.serverLoadAverageService.operateServerLoadAverage(serverPackage);
        // 把服务器平均负载历史记录添加到数据库
        this.serverLoadAverageHistoryService.operateServerLoadAverageHistory(serverPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
    }

    /**
     * <p>
     * 把服务器信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/21 13:21
     */
    @Retryable
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void operateServer(ServerPackage serverPackage) {
        // IP地址
        String ip = serverPackage.getIp();
        // 当前时间
        Date currentTime = new Date();
        // 查询数据库中是否有此IP的服务器
        LambdaQueryWrapper<MonitorServer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServer::getIp, ip);
        int selectCountDb = this.count(lambdaQueryWrapper);
        // 封装对象
        MonitorServer monitorServer = new MonitorServer();
        monitorServer.setIp(ip);
        monitorServer.setServerName(serverPackage.getComputerName());
        monitorServer.setConnFrequency((int) serverPackage.getRate());
        // 没有
        if (selectCountDb == 0) {
            monitorServer.setInsertTime(currentTime);
            monitorServer.setOfflineCount(0);
            // 默认开启监控和告警
            monitorServer.setIsEnableMonitor(ZeroOrOneConstants.ONE);
            monitorServer.setIsEnableAlarm(ZeroOrOneConstants.ONE);
            this.save(monitorServer);
        }
        // 有
        else {
            monitorServer.setUpdateTime(currentTime);
            LambdaUpdateWrapper<MonitorServer> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorServer::getIp, ip);
            this.update(monitorServer, lambdaUpdateWrapper);
        }
    }

    /**
     * <p>
     * 清理服务器历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2021/12/9 20:59
     */
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public int clearHistoryData(Date historyTime) {
        LambdaUpdateWrapper<MonitorServerProcessHistory> serverProcessHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverProcessHistoryLambdaUpdateWrapper.le(MonitorServerProcessHistory::getInsertTime, historyTime);
        int deleteServerProcessHistoryNum = this.monitorServerProcessHistoryDao.delete(serverProcessHistoryLambdaUpdateWrapper);
        LambdaUpdateWrapper<MonitorServerCpuHistory> serverCpuHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverCpuHistoryLambdaUpdateWrapper.le(MonitorServerCpuHistory::getInsertTime, historyTime);
        int deleteServerCpuHistoryNum = this.monitorServerCpuHistoryDao.delete(serverCpuHistoryLambdaUpdateWrapper);
        LambdaUpdateWrapper<MonitorServerDiskHistory> serverDiskHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverDiskHistoryLambdaUpdateWrapper.le(MonitorServerDiskHistory::getInsertTime, historyTime);
        int deleteServerDiskHistoryNum = this.monitorServerDiskHistoryDao.delete(serverDiskHistoryLambdaUpdateWrapper);
        LambdaUpdateWrapper<MonitorServerMemoryHistory> serverMemoryHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverMemoryHistoryLambdaUpdateWrapper.le(MonitorServerMemoryHistory::getInsertTime, historyTime);
        int deleteServerMemoryHistoryNum = this.monitorServerMemoryHistoryDao.delete(serverMemoryHistoryLambdaUpdateWrapper);
        LambdaUpdateWrapper<MonitorServerNetcardHistory> serverNetcardHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverNetcardHistoryLambdaUpdateWrapper.le(MonitorServerNetcardHistory::getInsertTime, historyTime);
        int deleteServerNetcardHistoryNum = this.monitorServerNetcardHistoryDao.delete(serverNetcardHistoryLambdaUpdateWrapper);
        LambdaUpdateWrapper<MonitorServerLoadAverageHistory> serverLoadAverageHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        serverLoadAverageHistoryLambdaUpdateWrapper.le(MonitorServerLoadAverageHistory::getInsertTime, historyTime);
        int deleteServerLoadAverageHistoryNum = this.monitorServerLoadAverageHistoryDao.delete(serverLoadAverageHistoryLambdaUpdateWrapper);
        return deleteServerProcessHistoryNum
                + deleteServerCpuHistoryNum
                + deleteServerDiskHistoryNum
                + deleteServerMemoryHistoryNum
                + deleteServerNetcardHistoryNum
                + deleteServerLoadAverageHistoryNum;
    }

}
