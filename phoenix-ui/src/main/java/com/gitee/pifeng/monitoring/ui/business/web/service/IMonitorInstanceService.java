package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorInstance;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeInstanceVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorInstanceVo;

import java.util.List;

/**
 * <p>
 * 应用实例服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
public interface IMonitorInstanceService extends IService<MonitorInstance> {

    /**
     * <p>
     * 获取home页的应用实例信息
     * </p>
     *
     * @return home页的应用实例表现层对象
     * @author 皮锋
     * @custom.date 2020/8/4 16:01
     */
    HomeInstanceVo getHomeInstanceInfo();

    /**
     * <p>
     * 获取应用程序列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param instanceName 应用实例名
     * @param endpoint     端点
     * @param isOnline     应用状态
     * @param monitorEnv   监控环境
     * @param monitorGroup 监控分组
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2020/9/26 11:02
     */
    Page<MonitorInstanceVo> getMonitorInstanceList(Long current, Long size, String instanceName, String endpoint, String isOnline, String monitorEnv, String monitorGroup);

    /**
     * <p>
     * 删除应用程序
     * </p>
     *
     * @param monitorInstanceVos 应用程序信息
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/26 12:25
     */
    LayUiAdminResultVo deleteMonitorInstance(List<MonitorInstanceVo> monitorInstanceVos);

    /**
     * <p>
     * 清理应用程序监控历史数据
     * </p>
     *
     * @param instanceId 应用实例ID
     * @param time       时间
     * @return layUiAdmin响应对象：如果清理成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/7/21 22:06
     */
    LayUiAdminResultVo clearMonitorInstanceHistory(String instanceId, String time);

    /**
     * <p>
     * 根据条件获取应用程序信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return 应用程序信息
     * @author 皮锋
     * @custom.date 2021/8/28 20:22
     */
    MonitorInstanceVo getMonitorInstanceInfo(String instanceId);

    /**
     * <p>
     * 编辑应用程序信息
     * </p>
     *
     * @param monitorInstanceVo 应用程序信息
     * @return 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/8/28 20:45
     */
    LayUiAdminResultVo editMonitorInstance(MonitorInstanceVo monitorInstanceVo);
}
