package com.gitee.pifeng.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.server.business.server.dao.IMonitorNetDao;
import com.gitee.pifeng.server.business.server.entity.MonitorNet;
import com.gitee.pifeng.server.business.server.service.INetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网络信息服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/31 17:21
 */
@Service
public class NetServiceImpl implements INetService {

    /**
     * 网络信息数据访问对象
     */
    @Autowired
    private IMonitorNetDao monitorNetDao;

    /**
     * <p>
     * 获取网络信息列表
     * </p>
     *
     * @param ipSource IP地址（来源）
     * @return 网络信息列表
     * @author 皮锋
     * @custom.date 2020/11/23 9:09
     */
    @Override
    public List<MonitorNet> getMonitorNetList(String ipSource) {
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorNet::getIpSource, ipSource);
        return this.monitorNetDao.selectList(lambdaQueryWrapper);
    }

    /**
     * <p>
     * 更新网络信息
     * </p>
     *
     * @param monitorNet 网络信息
     * @return 更新记录数
     * @author 皮锋
     * @custom.date 2020/11/23 11:05
     */
    @Override
    public int updateMonitorNet(MonitorNet monitorNet) {
        return this.monitorNetDao.updateById(monitorNet);
    }

}
