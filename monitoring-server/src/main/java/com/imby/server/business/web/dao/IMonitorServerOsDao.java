package com.imby.server.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imby.server.business.web.entity.MonitorServerOs;

import java.util.Map;

/**
 * <p>
 * 服务器数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
public interface IMonitorServerOsDao extends BaseMapper<MonitorServerOs> {

    /**
     * <p>
     * 服务器类型统计
     * </p>
     *
     * @return 服务器类型统计信息
     * @author 皮锋
     * @custom.date 2020/9/22 13:37
     */
    Map<String, Object> getServerOsTypeStatistics();
}
