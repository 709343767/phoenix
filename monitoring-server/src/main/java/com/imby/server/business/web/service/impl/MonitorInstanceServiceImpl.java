package com.imby.server.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imby.server.business.web.dao.*;
import com.imby.server.business.web.entity.*;
import com.imby.server.business.web.service.IMonitorInstanceService;
import com.imby.server.business.web.vo.HomeInstanceVo;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorInstanceVo;
import com.imby.server.constant.WebResponseConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .instanceOnLineRate(map.get("instanceOnLineRate").toString())
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
     * @param isOnconnect  网络状态
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/26 11:02
     */
    @Override
    public Page<MonitorInstanceVo> getMonitorInstanceList(Long current, Long size, String instanceName, String endpoint, String isOnline, String isOnconnect) {
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
        if (StringUtils.isNotBlank(isOnconnect)) {
            lambdaQueryWrapper.eq(MonitorInstance::getIsOnconnect, isOnconnect);
        }
        IPage<MonitorInstance> monitorInstancePage = this.monitorInstanceDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorInstance> monitorInstances = monitorInstancePage.getRecords();
        // 转换成应用程序信息表现层对象
        List<MonitorInstanceVo> monitorInstanceVos = Lists.newLinkedList();
        for (MonitorInstance monitorInstance : monitorInstances) {
            MonitorInstanceVo monitorInstanceVo = MonitorInstanceVo.builder().build().convertFor(monitorInstance);
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
    @Override
    public LayUiAdminResultVo deleteMonitorInstance(List<MonitorInstanceVo> monitorInstanceVos) {
        List<String> ids = Lists.newArrayList();
        for (MonitorInstanceVo monitorInstanceVo : monitorInstanceVos) {
            ids.add(monitorInstanceVo.getInstanceId());
        }
        // 应用程序
        LambdaUpdateWrapper<MonitorInstance> monitorInstanceLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorInstanceLambdaUpdateWrapper.in(MonitorInstance::getInstanceId, ids);
        this.monitorInstanceDao.delete(monitorInstanceLambdaUpdateWrapper);
        // java虚拟机类加载信息表
        LambdaUpdateWrapper<MonitorJvmClassLoading> monitorJvmClassLoadingLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorJvmClassLoadingLambdaUpdateWrapper.in(MonitorJvmClassLoading::getInstanceId, ids);
        this.monitorJvmClassLoadingDao.delete(monitorJvmClassLoadingLambdaUpdateWrapper);
        // java虚拟机GC信息表
        LambdaUpdateWrapper<MonitorJvmGarbageCollector> monitorJvmGarbageCollectorLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorJvmGarbageCollectorLambdaUpdateWrapper.in(MonitorJvmGarbageCollector::getInstanceId, ids);
        this.monitorJvmGarbageCollectorDao.delete(monitorJvmGarbageCollectorLambdaUpdateWrapper);
        // java虚拟机内存信息表
        LambdaUpdateWrapper<MonitorJvmMemory> monitorJvmMemoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorJvmMemoryLambdaUpdateWrapper.in(MonitorJvmMemory::getInstanceId, ids);
        this.monitorJvmMemoryDao.delete(monitorJvmMemoryLambdaUpdateWrapper);
        // java虚拟机运行时信息表
        LambdaUpdateWrapper<MonitorJvmRuntime> monitorJvmRuntimeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorJvmRuntimeLambdaUpdateWrapper.in(MonitorJvmRuntime::getInstanceId, ids);
        this.monitorJvmRuntimeDao.delete(monitorJvmRuntimeLambdaUpdateWrapper);
        // java虚拟机线程信息表
        LambdaUpdateWrapper<MonitorJvmThread> monitorJvmThreadLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorJvmThreadLambdaUpdateWrapper.in(MonitorJvmThread::getInstanceId, ids);
        this.monitorJvmThreadDao.delete(monitorJvmThreadLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

}
