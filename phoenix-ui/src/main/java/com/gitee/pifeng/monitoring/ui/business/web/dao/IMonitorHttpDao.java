package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttp;

import java.util.Map;

/**
 * <p>
 * HTTP信息表数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-11
 */
public interface IMonitorHttpDao extends BaseMapper<MonitorHttp> {

    /**
     * <p>
     * HTTP正常率统计
     * </p>
     *
     * @return HTTP正常率统计信息
     * @author 皮锋
     * @custom.date 2022/4/23 13:27
     */
    Map<String, Object> getHttpNormalRateStatistics();
}
