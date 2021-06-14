package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogOperation;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogOperationVo;

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
     * @param current 当前页
     * @param size    每页显示条数
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/6/14 21:28
     */
    Page<MonitorLogOperationVo> getMonitorLogOperationList(Long current, Long size);
}
