package com.imby.server.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imby.server.business.web.entity.MonitorAlarmDefinition;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorAlarmDefinitionVo;

import java.util.List;

/**
 * <p>
 * 告警定义服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
public interface IMonitorAlarmDefinitionService extends IService<MonitorAlarmDefinition> {

    /**
     * <p>
     * 获取告警定义列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param type    告警类型
     * @param grade   告警级别
     * @param title   告警标题
     * @param content 告警内容
     * @return 分页Page对象
     * @author 皮锋
     * @custom.date 2020/8/6 20:29
     */
    Page<MonitorAlarmDefinitionVo> getMonitorAlarmDefinitionList(Long current, Long size, String type, String grade, String title, String content);

    /**
     * <p>
     * 添加告警定义
     * </p>
     *
     * @param monitorAlarmDefinitionVo 告警定义
     * @return layUiAdmin响应对象：如果数据库中已经有此告警定义，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 12:22
     */
    LayUiAdminResultVo saveMonitorAlarmDefinition(MonitorAlarmDefinitionVo monitorAlarmDefinitionVo);

    /**
     * <p>
     * 编辑告警定义
     * </p>
     *
     * @param monitorAlarmDefinitionVo 告警定义
     * @return layUiAdmin响应对象：如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 15:26
     */
    LayUiAdminResultVo editMonitorAlarmDefinition(MonitorAlarmDefinitionVo monitorAlarmDefinitionVo);

    /**
     * <p>
     * 删除告警定义
     * </p>
     *
     * @param monitorAlarmDefinitionVos 告警定义
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 15:36
     */
    LayUiAdminResultVo deleteMonitorAlarmDefinition(List<MonitorAlarmDefinitionVo> monitorAlarmDefinitionVos);
}
