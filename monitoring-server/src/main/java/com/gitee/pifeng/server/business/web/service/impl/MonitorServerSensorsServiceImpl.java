package com.gitee.pifeng.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.server.business.web.dao.IMonitorServerSensorsDao;
import com.gitee.pifeng.server.business.web.entity.MonitorServerSensors;
import com.gitee.pifeng.server.business.web.service.IMonitorServerSensorsService;
import com.gitee.pifeng.server.business.web.vo.MonitorServerSensorsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务器传感器服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-15
 */
@Service
public class MonitorServerSensorsServiceImpl extends ServiceImpl<IMonitorServerSensorsDao, MonitorServerSensors> implements IMonitorServerSensorsService {

    /**
     * 服务器传感器数据访问对象
     */
    @Autowired
    private IMonitorServerSensorsDao monitorServerSensorsDao;

    /**
     * <p>
     * 获取服务器详情页面服务器传感器信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器传感器信息表现层对象
     * @author 皮锋
     * @custom.date 2021/1/16 23:06
     */
    @Override
    public MonitorServerSensorsVo getServerDetailPageServerSensorsInfo(String ip) {
        LambdaQueryWrapper<MonitorServerSensors> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerSensors::getIp, ip);
        MonitorServerSensors monitorServerSensors = this.monitorServerSensorsDao.selectOne(lambdaQueryWrapper);
        return MonitorServerSensorsVo.builder().build().convertFor(monitorServerSensors);
    }

}
