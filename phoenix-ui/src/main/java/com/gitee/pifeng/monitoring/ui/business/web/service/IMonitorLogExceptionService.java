package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogException;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogExceptionVo;

import java.util.List;

/**
 * <p>
 * 异常日志服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
public interface IMonitorLogExceptionService extends IService<MonitorLogException> {

    /**
     * <p>
     * 获取异常日志列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param instanceId   应用实例ID
     * @param instanceName 应用实例名
     * @param excName      异常名称
     * @param excMessage   异常信息
     * @param operMethod   操作方法
     * @param uri          请求URI
     * @param ip           请求IP
     * @param insertTime   插入时间
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/6/18 9:01
     */
    Page<MonitorLogExceptionVo> getMonitorLogExceptionList(Long current, Long size,
                                                           String instanceId, String instanceName,
                                                           String excName, String excMessage,
                                                           String uri, String ip,
                                                           String operMethod, String insertTime);

    /**
     * <p>
     * 删除异常日志
     * </p>
     *
     * @param ids 异常日志ID列表
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/6/18 12:37
     */
    LayUiAdminResultVo deleteMonitorLogException(List<Long> ids);

    /**
     * <p>
     * 清空异常日志
     * </p>
     *
     * @return layUiAdmin响应对象：如果清空成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/7/13 15:21
     */
    LayUiAdminResultVo cleanupMonitorLogException();

    /**
     * <p>
     * 获取异常日志信息
     * </p>
     *
     * @param id 异常日志ID
     * @return 异常日志表现层对象
     * @author 皮锋
     * @custom.date 2021/6/18 16:31
     */
    MonitorLogExceptionVo getMonitorLogExceptionInfo(Long id);

}
