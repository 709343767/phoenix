package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorGroupVo;

import java.util.List;

/**
 * <p>
 * 监控分组服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
public interface IMonitorGroupService extends IService<MonitorGroup> {

    /**
     * <p>
     * 获取监控分组列表
     * </p>
     *
     * @param current   当前页
     * @param size      每页显示条数
     * @param groupName 分组名称
     * @param groupDesc 分组描述
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/12/24 14:32
     */
    Page<MonitorGroupVo> getMonitorGroupList(Long current, Long size, String groupName, String groupDesc);

    /**
     * <p>
     * 添加分组信息
     * </p>
     *
     * @param monitorGroupVo 监控分组信息表现层对象
     * @return layUiAdmin响应对象：如果已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 14:33
     */
    LayUiAdminResultVo saveMonitorGroup(MonitorGroupVo monitorGroupVo);

    /**
     * <p>
     * 编辑分组信息
     * </p>
     *
     * @param monitorGroupVo 监控分组信息表现层对象
     * @return layUiAdmin响应对象：如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 14:34
     */
    LayUiAdminResultVo editMonitorGroup(MonitorGroupVo monitorGroupVo);

    /**
     * <p>
     * 删除分组信息
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/27 12:28
     */
    LayUiAdminResultVo deleteMonitorGroup(List<Long> ids);
}
