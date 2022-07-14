package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogException;

/**
 * <p>
 * 异常日志数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
public interface IMonitorLogExceptionDao extends BaseMapper<MonitorLogException> {

    /**
     * <p>
     * 清空异常日志
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/7/13 15:22
     */
    void cleanupMonitorLogException();

}
