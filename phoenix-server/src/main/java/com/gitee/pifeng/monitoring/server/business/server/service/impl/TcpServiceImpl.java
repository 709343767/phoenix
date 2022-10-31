package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorTcpDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcp;
import com.gitee.pifeng.monitoring.server.business.server.service.ITcpService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * TCP信息服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/11 16:25
 */
@Service
public class TcpServiceImpl extends ServiceImpl<IMonitorTcpDao, MonitorTcp> implements ITcpService {

    /**
     * <p>
     * 测试TCP连通性
     * </p>
     *
     * @param hostnameTarget 主机名（目的地）
     * @param portTarget     端口号
     * @return true 或者 false
     * @author 皮锋
     * @custom.date 2022/10/10 22:04
     */
    @Override
    public Boolean testMonitorTcp(String hostnameTarget, Integer portTarget) {
        // 测试telnet能否成功
        Map<String, Object> telnet = NetUtils.telnetVT200(hostnameTarget, portTarget);
        // 是否能telnet通
        boolean isConnected = Boolean.parseBoolean(String.valueOf(telnet.get("isConnect")));
        // 平均时间（毫秒）
        Long avgTime = Long.valueOf(String.valueOf(telnet.get("avgTime")));
        // 获取TCP信息
        LambdaQueryWrapper<MonitorTcp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorTcp::getHostnameSource, NetUtils.getLocalIp());
        lambdaQueryWrapper.eq(MonitorTcp::getHostnameTarget, hostnameTarget);
        lambdaQueryWrapper.eq(MonitorTcp::getPortTarget, portTarget);
        MonitorTcp monitorTcp = this.getOne(lambdaQueryWrapper);
        // 如果有TCP信息，则更新
        if (monitorTcp != null) {
            monitorTcp.setStatus(isConnected ? ZeroOrOneConstants.ONE : ZeroOrOneConstants.ZERO);
            monitorTcp.setAvgTime(avgTime);
            monitorTcp.setUpdateTime(new Date());
            this.updateById(monitorTcp);
        }
        return isConnected;
    }

}
