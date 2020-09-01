package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.server.business.web.dao.IMonitorNetDao;
import com.imby.server.business.web.entity.MonitorNet;
import com.imby.server.business.web.service.IMonitorNetService;
import com.imby.server.business.web.vo.HomeNetVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网络信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/1 15:09
 */
@Service
public class MonitorNetServiceImpl extends ServiceImpl<IMonitorNetDao, MonitorNet> implements IMonitorNetService {

    /**
     * 网络信息数据访问对象
     */
    @Autowired
    private IMonitorNetDao monitorNetDao;

    /**
     * <p>
     * 获取home页的网络信息
     * </p>
     *
     * @return home页的网络信息表现层对象
     * @author 皮锋
     * @custom.date 2020/9/1 15:20
     */
    @Override
    public HomeNetVo getHomeNetInfo() {
        List<MonitorNet> monitorNets = this.monitorNetDao.selectList(new QueryWrapper<>());
        // home页的网络信息表现层对象
        HomeNetVo homeNetVo = new HomeNetVo();
        homeNetVo.setNetSum(monitorNets.size());
        homeNetVo.setNetConnectSum((int) monitorNets.stream().filter(e -> StringUtils.equals(e.getStatus(), ZeroOrOneConstants.ONE)).count());
        homeNetVo.setNetDisconnectSum((int) monitorNets.stream().filter(e -> StringUtils.equals(e.getStatus(), ZeroOrOneConstants.ZERO)).count());
        homeNetVo.setNetConnectRate(String.format("%.2f",
                (double) homeNetVo.getNetConnectSum() / (double) homeNetVo.getNetSum() * 100D));
        return homeNetVo;
    }

}
