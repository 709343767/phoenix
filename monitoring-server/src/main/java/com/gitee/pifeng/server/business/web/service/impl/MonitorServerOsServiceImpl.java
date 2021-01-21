package com.gitee.pifeng.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.server.business.web.dao.IMonitorServerOsDao;
import com.gitee.pifeng.server.business.web.entity.MonitorServerOs;
import com.gitee.pifeng.server.business.web.service.IMonitorServerOsService;
import com.gitee.pifeng.server.business.web.vo.MonitorServerOsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器操作系统服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-21
 */
@Service
public class MonitorServerOsServiceImpl extends ServiceImpl<IMonitorServerOsDao, MonitorServerOs> implements IMonitorServerOsService {

    /**
     * 服务器操作系统数据访问对象
     */
    @Autowired
    private IMonitorServerOsDao monitorServerOsDao;

    /**
     * <p>
     * 获取服务器操作系统信息
     * </p>
     *
     * @param ip IP地址
     * @return 服务器操作系统信息表现层对象
     * @author 皮锋
     * @custom.date 2021/1/21 14:40
     */
    @Override
    public MonitorServerOsVo getMonitorServerOsInfo(String ip) {
        LambdaQueryWrapper<MonitorServerOs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerOs::getIp, ip);
        MonitorServerOs monitorServerOs = monitorServerOsDao.selectOne(lambdaQueryWrapper);
        return MonitorServerOsVo.builder().build().convertFor(monitorServerOs);
    }

}
