package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpIp;

import java.util.Map;

/**
 * <p>
 * TCP/IP信息数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-01-10
 */
public interface IMonitorTcpIpDao extends BaseMapper<MonitorTcpIp> {

    /**
     * <p>
     * TCP/IP正常率统计
     * </p>
     *
     * @return TCP/IP正常率统计信息
     * @author 皮锋
     * @custom.date 2022/1/27 10:46
     */
    Map<String, Object> getTcpIpNormalRateStatistics();
}
