package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.server.business.web.dao.IMonitorInstanceDao;
import com.imby.server.business.web.entity.MonitorInstance;
import com.imby.server.business.web.service.IMonitorInstanceService;
import com.imby.server.business.web.vo.HomeInstanceVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // 应用实例列表
        List<MonitorInstance> instances = this.monitorInstanceDao.selectList(new LambdaQueryWrapper<>());
        // home页的应用实例表现层对象
        HomeInstanceVo homeInstanceVo = new HomeInstanceVo();
        homeInstanceVo.setInstanceSum(instances.size());
        homeInstanceVo.setInstanceOnLineSum((int) instances.stream().filter(e -> StringUtils.equals(e.getIsOnline(), ZeroOrOneConstants.ONE)).count());
        homeInstanceVo.setInstanceOffLineSum((int) instances.stream().filter(e -> StringUtils.equals(e.getIsOnline(), ZeroOrOneConstants.ZERO)).count());
        return homeInstanceVo;
    }
}
