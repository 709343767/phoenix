package com.gitee.pifeng.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.server.business.web.dao.IMonitorServerPowerSourcesDao;
import com.gitee.pifeng.server.business.web.entity.MonitorServerPowerSources;
import com.gitee.pifeng.server.business.web.service.IMonitorServerPowerSourcesService;
import com.gitee.pifeng.server.business.web.vo.MonitorServerPowerSourcesVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务器电池服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-15
 */
@Service
public class MonitorServerPowerSourcesServiceImpl extends ServiceImpl<IMonitorServerPowerSourcesDao, MonitorServerPowerSources> implements IMonitorServerPowerSourcesService {

    /**
     * 服务器电池数据访问对象
     */
    @Autowired
    private IMonitorServerPowerSourcesDao monitorServerPowerSourcesDao;

    /**
     * <p>
     * 获取服务器详情页面服务器电池信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器电池信息表现层对象
     * @author 皮锋
     * @custom.date 2021/1/16 17:48
     */
    @Override
    public List<MonitorServerPowerSourcesVo> getServerDetailPageServerPowerSourcesInfo(String ip) {
        LambdaQueryWrapper<MonitorServerPowerSources> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerPowerSources::getIp, ip);
        List<MonitorServerPowerSources> monitorServerPowerSourcesList = this.monitorServerPowerSourcesDao.selectList(lambdaQueryWrapper);
        // 返回值
        List<MonitorServerPowerSourcesVo> result = Lists.newLinkedList();
        for (MonitorServerPowerSources powerSources : monitorServerPowerSourcesList) {
            MonitorServerPowerSourcesVo powerSourcesVo = MonitorServerPowerSourcesVo.builder().build().convertFor(powerSources);
            result.add(powerSourcesVo);
        }
        return result;
    }

}
