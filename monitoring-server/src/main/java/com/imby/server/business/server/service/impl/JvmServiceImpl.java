package com.imby.server.business.server.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.common.constant.ResultMsgConstants;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.common.domain.Result;
import com.imby.common.domain.jvm.RuntimeDomain;
import com.imby.common.dto.JvmPackage;
import com.imby.server.business.server.dao.IMonitorJvmRuntimeDao;
import com.imby.server.business.server.entity.MonitorJvmRuntime;
import com.imby.server.business.server.service.IJvmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * java虚拟机运行时信息数据访问对象
     */
    @Autowired
    private IMonitorJvmRuntimeDao monitorJvmRuntimeDao;

    /**
     * <p>
     * 处理java虚拟机信息包
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/8/27 17:45
     */
    @Override
    public Result dealJvmPackage(JvmPackage jvmPackage) {
        // 把java虚拟机运行时信息添加或更新到数据库
        this.operateMonitorJvmRuntime(jvmPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
    }

    /**
     * <p>
     * 把java虚拟机运行时信息添加或更新到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/27 19:20
     */
    private void operateMonitorJvmRuntime(JvmPackage jvmPackage) {
        // 应用实例ID
        String instanceId = jvmPackage.getInstanceId();
        // JVM运行时信息
        RuntimeDomain runtimeDomain = jvmPackage.getJvm().getRuntimeDomain();
        LambdaQueryWrapper<MonitorJvmRuntime> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorJvmRuntime::getInstanceId, instanceId);
        MonitorJvmRuntime monitorJvmRuntimeDb = this.monitorJvmRuntimeDao.selectOne(lambdaQueryWrapper);
        MonitorJvmRuntime monitorJvmRuntime = new MonitorJvmRuntime();
        monitorJvmRuntime.setInstanceId(instanceId);
        monitorJvmRuntime.setName(runtimeDomain.getName());
        monitorJvmRuntime.setVmName(runtimeDomain.getVmName());
        monitorJvmRuntime.setVmVendor(runtimeDomain.getVmVendor());
        monitorJvmRuntime.setVmVersion(runtimeDomain.getVmVersion());
        monitorJvmRuntime.setSpecName(runtimeDomain.getSpecName());
        monitorJvmRuntime.setSpecVendor(runtimeDomain.getSpecVendor());
        monitorJvmRuntime.setSpecVersion(runtimeDomain.getSpecVersion());
        monitorJvmRuntime.setManagementSpecVersion(runtimeDomain.getManagementSpecVersion());
        monitorJvmRuntime.setClassPath(runtimeDomain.getClassPath());
        monitorJvmRuntime.setLibraryPath(runtimeDomain.getLibraryPath());
        monitorJvmRuntime.setIsBootClassPathSupported(runtimeDomain.isBootClassPathSupported() ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
        monitorJvmRuntime.setBootClassPath(runtimeDomain.getBootClassPath());
        monitorJvmRuntime.setInputArguments(ArrayUtil.join(runtimeDomain.getInputArguments().toArray(new String[0]), ";"));
        monitorJvmRuntime.setUptime(runtimeDomain.getUptime());
        monitorJvmRuntime.setStartTime(runtimeDomain.getStartTime());
        // 新增java虚拟机运行时信息
        if (monitorJvmRuntimeDb == null) {
            monitorJvmRuntime.setInsertTime(jvmPackage.getDateTime());
            this.monitorJvmRuntimeDao.insert(monitorJvmRuntime);
        }
        // 更新java虚拟机运行时信息
        else {
            monitorJvmRuntime.setUpdateTime(jvmPackage.getDateTime());
            LambdaUpdateWrapper<MonitorJvmRuntime> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(MonitorJvmRuntime::getInstanceId, instanceId);
            this.monitorJvmRuntimeDao.update(monitorJvmRuntime, lambdaUpdateWrapper);
        }
    }
}
