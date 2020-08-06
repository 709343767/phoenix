package com.imby.server.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imby.server.business.web.entity.MonitorAlarmDefinition;
import com.imby.server.business.web.vo.MonitorAlarmDefinitionVo;

/**
 * <p>
 * 告警定义服务类
 * </p>
 *
 * @author 皮锋
 * @since 2020-08-06
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
}
