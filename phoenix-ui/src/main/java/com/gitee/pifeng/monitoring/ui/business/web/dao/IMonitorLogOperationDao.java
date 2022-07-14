package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogOperation;

/**
 * <p>
 * 操作日志数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
public interface IMonitorLogOperationDao extends BaseMapper<MonitorLogOperation> {

    /**
     * <p>
     * 清空操作日志
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/7/13 15:41
     */
    void cleanupMonitorLogOperation();

}
