package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorNet;

import java.util.Map;

/**
 * <p>
 * 网络信息数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/1 15:11
 */
public interface IMonitorNetDao extends BaseMapper<MonitorNet> {

    /**
     * <p>
     * 网络正常率统计
     * </p>
     *
     * @return 网络正常率统计信息
     * @author 皮锋
     * @custom.date 2020/9/22 13:14
     */
    Map<String, Object> getNetNormalRateStatistics();

}
