package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogOperation;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogOperationVo;

import java.util.List;

/**
 * <p>
 * 操作日志服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
public interface IMonitorLogOperationService extends IService<MonitorLogOperation> {

    /**
     * <p>
     * 获取操作日志列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param operModule 功能模块
     * @param operDesc   操作描述
     * @param operType   操作类型
     * @param username   操作用户
     * @param ip         请求IP
     * @param insertTime 插入时间
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/6/14 21:28
     */
    Page<MonitorLogOperationVo> getMonitorLogOperationList(Long current, Long size, String operModule,
                                                           String operDesc, String operType,
                                                           String username, String ip, String insertTime);

    /**
     * <p>
     * 删除操作日志
     * </p>
     *
     * @param ids 操作日志ID
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/6/15 14:38
     */
    LayUiAdminResultVo deleteMonitorLogOperation(List<Long> ids);

    /**
     * <p>
     * 清空操作日志
     * </p>
     *
     * @return layUiAdmin响应对象：如果清空成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/7/13 15:39
     */
    LayUiAdminResultVo cleanupMonitorLogOperation();

    /**
     * <p>
     * 获取操作日志信息
     * </p>
     *
     * @param id 操作日志ID
     * @return 操作日志表现层对象
     * @author 皮锋
     * @custom.date 2021/6/18 16:25
     */
    MonitorLogOperationVo getMonitorLogOperationInfo(Long id);

}
