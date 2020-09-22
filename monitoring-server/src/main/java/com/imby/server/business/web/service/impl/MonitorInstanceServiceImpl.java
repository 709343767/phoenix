package com.imby.server.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.server.business.web.dao.IMonitorInstanceDao;
import com.imby.server.business.web.entity.MonitorInstance;
import com.imby.server.business.web.service.IMonitorInstanceService;
import com.imby.server.business.web.vo.HomeInstanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
