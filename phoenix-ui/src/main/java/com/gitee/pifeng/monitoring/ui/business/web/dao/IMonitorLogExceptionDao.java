package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogException;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogExceptionVo;

import java.util.Date;

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

    /**
     * <p>
     * 分页查询异常日志
     * </p>
     *
     * @param page          分页参数
     * @param instanceId    应用实例ID
     * @param instanceName  应用实例名
     * @param excName       异常名称
     * @param excMessage    异常信息
     * @param operMethod    操作方法
     * @param uri           请求URI
     * @param ip            请求IP
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return {@link IPage} 一页异常日志数据
     * @author 皮锋
     * @custom.date 2024/2/29 16:48
     */
    IPage<MonitorLogExceptionVo> getMonitorLogExceptionList(IPage<MonitorLogException> page, String instanceId,
                                                            String instanceName, String excName,
                                                            String excMessage, String operMethod,
                                                            String uri, String ip,
                                                            Date startDateTime, Date endDateTime);

    /**
     * <p>
     * 根据异常日志ID获取异常日志信息
     * </p>
     *
     * @param id 异常日志ID
     * @return {@link MonitorLogExceptionVo} 异常日志表现层对象
     * @author 皮锋
     * @custom.date 2024/3/1 10:16
     */
    MonitorLogExceptionVo getMonitorLogExceptionById(Long id);

}
