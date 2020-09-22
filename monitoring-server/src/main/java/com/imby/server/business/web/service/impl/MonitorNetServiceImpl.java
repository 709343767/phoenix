package com.imby.server.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.server.business.web.dao.IMonitorNetDao;
import com.imby.server.business.web.entity.MonitorNet;
import com.imby.server.business.web.service.IMonitorNetService;
import com.imby.server.business.web.vo.HomeNetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        // 网络正常率统计
        Map<String, Object> map = this.monitorNetDao.getNetNormalRateStatistics();
        return HomeNetVo.builder()
                .netSum(NumberUtil.parseInt(map.get("netSum").toString()))
                .netConnectSum(NumberUtil.parseInt(map.get("netConnectSum").toString()))
                .netDisconnectSum(NumberUtil.parseInt(map.get("netDisconnectSum").toString()))
                .netConnectRate(map.get("netConnectRate").toString())
                .build();
    }

}
