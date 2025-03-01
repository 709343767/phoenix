package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorJvmMemoryHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorInstance;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmMemoryHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * java虚拟机信息服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/27 17:42
 */
@Service
public class JvmServiceImpl implements IJvmService {

    /**
     * 应用实例服务接口
     */
    @Autowired
    private IInstanceService instanceService;

    /**
     * java虚拟机运行时信息服务接口
     */
    @Autowired
    private IJvmRuntimeService jvmRuntimeService;

    /**
     * java虚拟机类加载信息服务接口
     */
    @Autowired
    private IJvmClassLoadingService jvmClassLoadingService;

    /**
     * java虚拟机内存信息服务接口
     */
    @Autowired
    private IJvmMemoryService jvmMemoryService;

    /**
     * java虚拟机内存历史记录服务接口
     */
    @Autowired
    private IJvmMemoryHistoryService jvmMemoryHistoryService;

    /**
     * java虚拟机线程信息服务接口
     */
    @Autowired
    private IJvmThreadService jvmThreadService;

    /**
     * java虚拟机GC信息服务接口
     */
    @Autowired
    private IJvmGarbageCollectorService jvmGarbageCollectorService;

    /**
     * java虚拟机内存历史记录数据访问对象
     */
    @Autowired
    private IMonitorJvmMemoryHistoryDao monitorJvmMemoryHistoryDao;

    /**
     * <p>
     * 处理java虚拟机信息包
     * </p>
     * 此处不加事务，因为操作的表太多，数据太多，不加事务能提高并发性能，而且此处对数据的一致性要求并不是很高。
     *
     * @param jvmPackage java虚拟机信息包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/8/27 17:45
     */
    //@Transactional(rollbackFor = Throwable.class)
    @Override
    public Result dealJvmPackage(JvmPackage jvmPackage) {
        // 先判断有没有此应用实例
        LambdaQueryWrapper<MonitorInstance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorInstance::getInstanceId, jvmPackage.getInstanceId());
        int count = this.instanceService.count(lambdaQueryWrapper);
        if (count == 0) {
            return Result.builder().isSuccess(false).msg(ResultMsgConstants.FAILURE).build();
        }
        // 把java虚拟机运行时信息添加或更新到数据库
        this.jvmRuntimeService.operateMonitorJvmRuntime(jvmPackage);
        // 把java虚拟机类加载信息添加或更新到数据库
        this.jvmClassLoadingService.operateMonitorJvmClassLoading(jvmPackage);
        // 把java虚拟机内存信息添加或更新到数据库
        this.jvmMemoryService.operateMonitorJvmMemory(jvmPackage);
        // 把java虚拟机内存历史信息添加到数据库
        this.jvmMemoryHistoryService.operateMonitorJvmMemoryHistory(jvmPackage);
        // 把java虚拟机线程信息添加或更新到数据库
        this.jvmThreadService.operateMonitorJvmThread(jvmPackage);
        // 把java虚拟机GC信息添加或更新到数据库
        this.jvmGarbageCollectorService.operateMonitorJvmGarbageCollector(jvmPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
    }

    /**
     * <p>
     * 清理JVM历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2021/12/9 20:46
     */
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public int clearHistoryData(Date historyTime) {
        LambdaUpdateWrapper<MonitorJvmMemoryHistory> jvmMemoryHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        jvmMemoryHistoryLambdaUpdateWrapper.le(MonitorJvmMemoryHistory::getInsertTime, historyTime);
        return this.monitorJvmMemoryHistoryDao.delete(jvmMemoryHistoryLambdaUpdateWrapper);
    }

}
