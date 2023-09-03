package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorNetDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNet;
import com.gitee.pifeng.monitoring.server.business.server.service.INetService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 网络信息服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/26 15:28
 */
@Service
public class NetServiceImpl extends ServiceImpl<IMonitorNetDao, MonitorNet> implements INetService {

    /**
     * <p>
     * 获取被监控网络源IP地址
     * </p>
     *
     * @return 被监控网络源IP地址
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2021/10/6 22:05
     */
    @Override
    public String getSourceIp() throws NetException {
        return NetUtils.getLocalIp();
    }

    /**
     * <p>
     * 测试网络连通性
     * </p>
     *
     * @param ipTarget IP地址（目的地）
     * @return true 或者 false
     * @author 皮锋
     * @custom.date 2022/10/10 22:04
     */
    @Override
    public Boolean testMonitorNetwork(String ipTarget) {
        Map<String, Object> objectMap = NetUtils.isConnect(ipTarget);
        // 测试IP地址能否ping通
        boolean isConnected = Boolean.parseBoolean(String.valueOf(objectMap.get("isConnect")));
        // 平均响应时间
        Double avgTime = Double.valueOf(String.valueOf(objectMap.get("avgTime")));
        // ping详情
        String detail = String.valueOf(objectMap.get("detail"));
        // 获取网络信息
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorNet::getIpSource, NetUtils.getLocalIp());
        lambdaQueryWrapper.eq(MonitorNet::getIpTarget, ipTarget);
        MonitorNet monitorNet = this.getOne(lambdaQueryWrapper);
        // 如果有网络信息，则更新
        if (monitorNet != null) {
            monitorNet.setStatus(isConnected ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
            monitorNet.setAvgTime(avgTime);
            monitorNet.setPingDetail(detail);
            monitorNet.setUpdateTime(new Date());
            // 更新数据库
            this.updateById(monitorNet);
        }
        return isConnected;
    }

}