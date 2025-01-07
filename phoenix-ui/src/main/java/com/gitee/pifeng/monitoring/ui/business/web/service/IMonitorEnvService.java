package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorEnvVo;

import java.util.List;

/**
 * <p>
 * 监控环境服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
public interface IMonitorEnvService extends IService<MonitorEnv> {

    /**
     * <p>
     * 获取监控环境列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param envName 环境名称
     * @param envDesc 环境描述
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/12/24 9:41
     */
    Page<MonitorEnvVo> getMonitorEnvList(Long current, Long size, String envName, String envDesc);

    /**
     * <p>
     * 添加环境信息
     * </p>
     *
     * @param monitorEnvVo 监控环境信息表现层对象
     * @return layUiAdmin响应对象：如果已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 10:31
     */
    LayUiAdminResultVo saveMonitorEnv(MonitorEnvVo monitorEnvVo);

    /**
     * <p>
     * 编辑环境信息
     * </p>
     *
     * @param monitorEnvVo 监控环境信息表现层对象
     * @return layUiAdmin响应对象：如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 11:11
     */
    LayUiAdminResultVo editMonitorEnv(MonitorEnvVo monitorEnvVo);

    /**
     * <p>
     * 删除环境信息
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/27 10:11
     */
    LayUiAdminResultVo deleteMonitorEnv(List<Long> ids);
}
