package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.*;
import com.gitee.pifeng.monitoring.ui.business.web.entity.*;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorInstanceService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeInstanceVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorInstanceVo;
import com.gitee.pifeng.monitoring.ui.constant.TimeSelectConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.CalculateDateTime;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应用实例服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@Service
public class MonitorInstanceServiceImpl extends ServiceImpl<IMonitorInstanceDao, MonitorInstance> implements IMonitorInstanceService {

    /**
     * 应用实例数据访问对象
     */
    @Autowired
    private IMonitorInstanceDao monitorInstanceDao;

    /**
     * java虚拟机类加载信息数据访问对象
     */
    @Autowired
    private IMonitorJvmClassLoadingDao monitorJvmClassLoadingDao;

    /**
     * java虚拟机GC信息数据访问对象
     */
    @Autowired
    private IMonitorJvmGarbageCollectorDao monitorJvmGarbageCollectorDao;

    /**
     * java虚拟机内存信息数据访问对象
     */
    @Autowired
    private IMonitorJvmMemoryDao monitorJvmMemoryDao;

    /**
     * java虚拟机内存历史记录数据访问对象
     */
    @Autowired
    private IMonitorJvmMemoryHistoryDao monitorJvmMemoryHistoryDao;

    /**
     * java虚拟机运行时信息数据访问对象
     */
    @Autowired
    private IMonitorJvmRuntimeDao monitorJvmRuntimeDao;

    /**
     * java虚拟机线程信息数据访问对象
     */
    @Autowired
    private IMonitorJvmThreadDao monitorJvmThreadDao;

    /**
     * <p>
     * 获取home页的应用实例信息
     * </p>
     *
     * @return home页的应用实例表现层对象
     * @author 皮锋
     * @custom.date 2020/8/4 16:01
     */
    @Override
    public HomeInstanceVo getHomeInstanceInfo() {
        // 应用实例在线率统计
        Map<String, Object> map = this.monitorInstanceDao.getInstanceOnlineRateStatistics();
        return HomeInstanceVo.builder()
                .instanceSum(NumberUtil.parseInt(map.get("instanceSum").toString()))
                .instanceOnLineSum(NumberUtil.parseInt(map.get("instanceOnLineSum").toString()))
                .instanceOffLineSum(NumberUtil.parseInt(map.get("instanceOffLineSum").toString()))
                .instanceOnLineRate(NumberUtil.round(map.get("instanceOnLineRate").toString(), 2).toString())
                .build();
    }

    /**
     * <p>
     * 获取应用程序列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param instanceName 应用实例名
     * @param endpoint     端点
     * @param isOnline     应用状态
     * @param monitorEnv   监控环境
     * @param monitorGroup 监控分组
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2020/9/26 11:02
     */
    @Override
    public Page<MonitorInstanceVo> getMonitorInstanceList(Long current, Long size, String instanceName, String endpoint, String isOnline, String monitorEnv, String monitorGroup) {
        // 查询数据库
        IPage<MonitorInstance> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorInstance> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(instanceName)) {
            lambdaQueryWrapper.like(MonitorInstance::getInstanceName, instanceName);
        }
        if (StringUtils.isNotBlank(endpoint)) {
            lambdaQueryWrapper.eq(MonitorInstance::getEndpoint, endpoint);
        }
        if (StringUtils.isNotBlank(isOnline)) {
            lambdaQueryWrapper.eq(MonitorInstance::getIsOnline, isOnline);
        }
        if (StringUtils.isNotBlank(monitorEnv)) {
            lambdaQueryWrapper.like(MonitorInstance::getMonitorEnv, monitorEnv);
        }
        if (StringUtils.isNotBlank(monitorGroup)) {
            lambdaQueryWrapper.like(MonitorInstance::getMonitorGroup, monitorGroup);
        }
        lambdaQueryWrapper.orderByAsc(MonitorInstance::getInstanceName).orderByAsc(MonitorInstance::getId);
        IPage<MonitorInstance> monitorInstancePage = this.monitorInstanceDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorInstance> monitorInstances = monitorInstancePage.getRecords();
        // 转换成应用程序信息表现层对象
        List<MonitorInstanceVo> monitorInstanceVos = Lists.newLinkedList();
        // 当前时间
        Date currentDateTime = new Date();
        for (MonitorInstance monitorInstance : monitorInstances) {
            MonitorInstanceVo monitorInstanceVo = MonitorInstanceVo.builder().build().convertFor(monitorInstance);
            // 如果应用实例摘要信息不为空，则把 应用实例摘要信息 赋给 应用实例描述。因为：摘要信息是用户通过UI界面设置的，优先级大于描述。
            String instanceSummary = monitorInstanceVo.getInstanceSummary();
            if (StringUtils.isNotBlank(instanceSummary)) {
                monitorInstanceVo.setInstanceDesc(instanceSummary);
            }
            Date updateTime = monitorInstance.getUpdateTime();
            // 最后心跳时间
            String finalHeartbeat = updateTime != null ? DateUtil.formatBetween(currentDateTime, updateTime, BetweenFormatter.Level.SECOND) + "前" : "";
            monitorInstanceVo.setFinalHeartbeat(finalHeartbeat);
            monitorInstanceVos.add(monitorInstanceVo);
        }
        // 设置返回对象
        Page<MonitorInstanceVo> monitorInstanceVoPage = new Page<>();
        monitorInstanceVoPage.setRecords(monitorInstanceVos);
        monitorInstanceVoPage.setTotal(monitorInstancePage.getTotal());
        return monitorInstanceVoPage;
    }

    /**
     * <p>
     * 删除应用程序
     * </p>
     *
     * @param monitorInstanceVos 应用程序信息
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/26 12:25
     */
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    @Retryable
    public LayUiAdminResultVo deleteMonitorInstance(List<MonitorInstanceVo> monitorInstanceVos) {
        List<String> instances = Lists.newArrayList();
        for (MonitorInstanceVo monitorInstanceVo : monitorInstanceVos) {
            instances.add(monitorInstanceVo.getInstanceId());
        }
        // java虚拟机类加载信息表
        LambdaUpdateWrapper<MonitorJvmClassLoading> monitorJvmClassLoadingLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorJvmClassLoadingLambdaUpdateWrapper.in(MonitorJvmClassLoading::getInstanceId, instances);
        this.monitorJvmClassLoadingDao.delete(monitorJvmClassLoadingLambdaUpdateWrapper);
        // java虚拟机GC信息表
        LambdaUpdateWrapper<MonitorJvmGarbageCollector> monitorJvmGarbageCollectorLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorJvmGarbageCollectorLambdaUpdateWrapper.in(MonitorJvmGarbageCollector::getInstanceId, instances);
        this.monitorJvmGarbageCollectorDao.delete(monitorJvmGarbageCollectorLambdaUpdateWrapper);
        // java虚拟机内存历史记录表
        LambdaUpdateWrapper<MonitorJvmMemoryHistory> jvmMemoryHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        jvmMemoryHistoryLambdaUpdateWrapper.in(MonitorJvmMemoryHistory::getInstanceId, instances);
        this.monitorJvmMemoryHistoryDao.delete(jvmMemoryHistoryLambdaUpdateWrapper);
        // java虚拟机内存信息表
        LambdaUpdateWrapper<MonitorJvmMemory> monitorJvmMemoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorJvmMemoryLambdaUpdateWrapper.in(MonitorJvmMemory::getInstanceId, instances);
        this.monitorJvmMemoryDao.delete(monitorJvmMemoryLambdaUpdateWrapper);
        // java虚拟机运行时信息表
        LambdaUpdateWrapper<MonitorJvmRuntime> monitorJvmRuntimeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorJvmRuntimeLambdaUpdateWrapper.in(MonitorJvmRuntime::getInstanceId, instances);
        this.monitorJvmRuntimeDao.delete(monitorJvmRuntimeLambdaUpdateWrapper);
        // java虚拟机线程信息表
        LambdaUpdateWrapper<MonitorJvmThread> monitorJvmThreadLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorJvmThreadLambdaUpdateWrapper.in(MonitorJvmThread::getInstanceId, instances);
        this.monitorJvmThreadDao.delete(monitorJvmThreadLambdaUpdateWrapper);
        // 应用程序
        LambdaUpdateWrapper<MonitorInstance> monitorInstanceLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorInstanceLambdaUpdateWrapper.in(MonitorInstance::getInstanceId, instances);
        this.monitorInstanceDao.delete(monitorInstanceLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 清理应用程序监控历史数据
     * </p>
     *
     * @param instanceId 应用实例ID
     * @param time       时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/7/21 22:06
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public LayUiAdminResultVo clearMonitorInstanceHistory(String instanceId, String time) {
        // 时间为空
        if (StringUtils.isBlank(time)) {
            return LayUiAdminResultVo.ok(WebResponseConstants.REQUIRED_IS_NULL);
        }
        CalculateDateTime calculateDateTime = new CalculateDateTime(time).invoke();
        // 清理时间
        Date clearTime = calculateDateTime.getStartTime();
        // 清理所有时间点的数据，相当于清理当前时间前的数据
        if (StringUtils.equalsIgnoreCase(time, TimeSelectConstants.ALL)) {
            clearTime = new Date();
        }
        // java虚拟机内存历史记录表
        LambdaUpdateWrapper<MonitorJvmMemoryHistory> jvmMemoryHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        jvmMemoryHistoryLambdaUpdateWrapper.eq(MonitorJvmMemoryHistory::getInstanceId, instanceId);
        jvmMemoryHistoryLambdaUpdateWrapper.lt(MonitorJvmMemoryHistory::getInsertTime, clearTime);
        this.monitorJvmMemoryHistoryDao.delete(jvmMemoryHistoryLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 根据条件获取应用程序信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return 应用程序信息
     * @author 皮锋
     * @custom.date 2021/8/28 20:22
     */
    @Override
    public MonitorInstanceVo getMonitorInstanceInfo(String instanceId) {
        LambdaQueryWrapper<MonitorInstance> monitorInstanceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorInstanceLambdaQueryWrapper.eq(MonitorInstance::getInstanceId, instanceId);
        MonitorInstance monitorInstance = this.monitorInstanceDao.selectOne(monitorInstanceLambdaQueryWrapper);
        return new MonitorInstanceVo().convertFor(monitorInstance);
    }

    /**
     * <p>
     * 编辑应用程序信息
     * </p>
     *
     * @param monitorInstanceVo 应用程序信息
     * @return 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/8/28 20:45
     */
    @Override
    public LayUiAdminResultVo editMonitorInstance(MonitorInstanceVo monitorInstanceVo) {
        MonitorInstance monitorInstance = monitorInstanceVo.convertTo();
        if (StringUtils.isBlank(monitorInstance.getMonitorEnv())) {
            monitorInstance.setMonitorEnv(null);
        }
        if (StringUtils.isBlank(monitorInstance.getMonitorGroup())) {
            monitorInstance.setMonitorGroup(null);
        }
        LambdaUpdateWrapper<MonitorInstance> monitorInstanceLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorInstanceLambdaUpdateWrapper.eq(MonitorInstance::getInstanceId, monitorInstance.getInstanceId());
        this.monitorInstanceDao.update(monitorInstance, monitorInstanceLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

}
