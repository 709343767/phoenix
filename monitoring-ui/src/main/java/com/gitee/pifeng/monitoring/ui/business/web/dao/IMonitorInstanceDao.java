package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorInstance;

import java.util.Map;

/**
 * <p>
 * 应用实例数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
public interface IMonitorInstanceDao extends BaseMapper<MonitorInstance> {

    /**
     * <p>
     * 应用实例在线率统计
     * </p>
     *
     * @return 应用实例线率统计信息
     * @author 皮锋
     * @custom.date 2020/9/22 12:31
     */
    Map<String, Object> getInstanceOnlineRateStatistics();
}
