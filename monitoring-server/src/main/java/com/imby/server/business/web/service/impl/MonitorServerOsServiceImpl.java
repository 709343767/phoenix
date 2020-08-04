package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.server.business.web.dao.IMonitorServerOsDao;
import com.imby.server.business.web.entity.MonitorServerOs;
import com.imby.server.business.web.service.IMonitorServerOsService;
import com.imby.server.business.web.vo.HomeServerOsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务器服务实现类
 * </p>
 *
 * @author 皮锋
 * @since 2020-08-04
 */
@Service
public class MonitorServerOsServiceImpl extends ServiceImpl<IMonitorServerOsDao, MonitorServerOs> implements IMonitorServerOsService {

    /**
     * 服务器数据访问对象
     */
    @Autowired
    private IMonitorServerOsDao monitorServerOsDao;

    /**
     * <p>
     * 获取home页的服务器信息
     * </p>
     *
     * @return home页的服务器表现层对象
     * @author 皮锋
     * @custom.date 2020/8/4 16:40
     */
    @Override
    public HomeServerOsVo getHomeServerOsInfo() {
        List<MonitorServerOs> monitorServerOss = this.monitorServerOsDao.selectList(new LambdaQueryWrapper<>());
        // home页的服务器表现层对象
        HomeServerOsVo homeServerOsVo = new HomeServerOsVo();
        homeServerOsVo.setServerSum(monitorServerOss.size());
        return homeServerOsVo;
    }
}
