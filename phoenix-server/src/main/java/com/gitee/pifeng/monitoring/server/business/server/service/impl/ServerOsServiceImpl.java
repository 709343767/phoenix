package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.domain.server.OsDomain;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorServerOsDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerOs;
import com.gitee.pifeng.monitoring.server.business.server.service.IServerOsService;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务器操作系统信息服务层实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 14:23
 */
@Service
public class ServerOsServiceImpl extends ServiceImpl<IMonitorServerOsDao, MonitorServerOs> implements IServerOsService {

    /**
     * <p>
     * 把服务器操作系统信息添加或更新到数据库
     * </p>
     * 此处不加事务，不加事务能提高并发性能，并且对数据的一致性要求也没那么高
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2020/5/11 16:01
     */
    @Retryable
    @Override
    public void operateServerOs(ServerPackage serverPackage) {
        // 操作系统信息
        OsDomain osDomain = serverPackage.getServer().getOsDomain();
        if (osDomain != null) {
            // IP地址
            String ip = serverPackage.getIp();
            // 当前时间
            Date currentTime = new Date();
            LambdaQueryWrapper<MonitorServerOs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MonitorServerOs::getIp, ip);
            int selectCountDb = this.count(lambdaQueryWrapper);
            // 封装对象
            MonitorServerOs monitorServerOs = new MonitorServerOs();
            monitorServerOs.setIp(ip);
            monitorServerOs.setServerName(osDomain.getComputerName());
            monitorServerOs.setOsName(osDomain.getOsName());
            monitorServerOs.setOsArch(osDomain.getOsArch());
            monitorServerOs.setOsVersion(osDomain.getOsVersion());
            monitorServerOs.setOsTimeZone(osDomain.getOsTimeZone());
            monitorServerOs.setUserHome(osDomain.getUserHome());
            monitorServerOs.setUserName(osDomain.getUserName());
            // 没有
            if (selectCountDb == 0) {
                monitorServerOs.setInsertTime(currentTime);
                this.save(monitorServerOs);
            }
            // 有
            else {
                monitorServerOs.setUpdateTime(currentTime);
                LambdaUpdateWrapper<MonitorServerOs> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(MonitorServerOs::getIp, ip);
                this.update(monitorServerOs, lambdaUpdateWrapper);
            }
        }
    }

}
