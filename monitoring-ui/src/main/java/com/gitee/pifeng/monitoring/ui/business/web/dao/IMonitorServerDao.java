package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServer;

import java.util.Map;

/**
 * <p>
 * 服务器数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
public interface IMonitorServerDao extends BaseMapper<MonitorServer> {

    /**
     * <p>
     * 服务器在线率统计
     * </p>
     *
     * @return 服务器在线率统计信息
     * @author 皮锋
     * @custom.date 2021/5/12 20:37
     */
    Map<String, Object> getServerOnlineRateStatistics();

}
