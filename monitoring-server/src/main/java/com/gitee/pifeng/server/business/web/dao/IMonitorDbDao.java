package com.gitee.pifeng.server.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.server.business.web.entity.MonitorDb;

import java.util.Map;

/**
 * <p>
 * 数据库表数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-12-19
 */
public interface IMonitorDbDao extends BaseMapper<MonitorDb> {

    /**
     * <p>
     * 数据库正常率统计
     * </p>
     *
     * @return 数据库正常率统计信息
     * @author 皮锋
     * @custom.date 2020/12/19 21:46
     */
    Map<String, Object> getDbNormalRateStatistics();

}
