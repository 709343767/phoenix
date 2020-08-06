package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imby.server.business.web.dao.IMonitorAlarmDefinitionDao;
import com.imby.server.business.web.entity.MonitorAlarmDefinition;
import com.imby.server.business.web.service.IMonitorAlarmDefinitionService;
import com.imby.server.business.web.vo.MonitorAlarmDefinitionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 告警定义服务实现类
 * </p>
 *
 * @author 皮锋
 * @since 2020-08-06
 */
@Service
public class MonitorAlarmDefinitionServiceImpl extends ServiceImpl<IMonitorAlarmDefinitionDao, MonitorAlarmDefinition> implements IMonitorAlarmDefinitionService {

    /**
     * 告警定义数据访问对象
     */
    @Autowired
    private IMonitorAlarmDefinitionDao monitorAlarmDefinitionDao;

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
    @Override
    public Page<MonitorAlarmDefinitionVo> getMonitorAlarmDefinitionList(Long current, Long size, String type, String grade, String title, String content) {
        // 查询数据库
        IPage<MonitorAlarmDefinition> iPage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorAlarmDefinition> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(type)) {
            lambdaQueryWrapper.eq(MonitorAlarmDefinition::getType, type);
        }
        if (StringUtils.isNotBlank(grade)) {
            lambdaQueryWrapper.eq(MonitorAlarmDefinition::getGrade, grade);
        }
        if (StringUtils.isNotBlank(title)) {
            lambdaQueryWrapper.like(MonitorAlarmDefinition::getTitle, title);
        }
        if (StringUtils.isNotBlank(content)) {
            lambdaQueryWrapper.like(MonitorAlarmDefinition::getContent, content);
        }
        IPage<MonitorAlarmDefinition> monitorAlarmDefinitionPage = this.monitorAlarmDefinitionDao.selectPage(iPage, lambdaQueryWrapper);
        List<MonitorAlarmDefinition> monitorAlarmDefinitions = monitorAlarmDefinitionPage.getRecords();
        // 转换成监控告警表现层对象
        List<MonitorAlarmDefinitionVo> monitorAlarmDefinitionVos = Lists.newLinkedList();
        for (MonitorAlarmDefinition monitorAlarmDefinition : monitorAlarmDefinitions) {
            MonitorAlarmDefinitionVo monitorAlarmDefinitionVo = MonitorAlarmDefinitionVo.builder().build().convertFor(monitorAlarmDefinition);
            monitorAlarmDefinitionVos.add(monitorAlarmDefinitionVo);
        }
        Page<MonitorAlarmDefinitionVo> monitorAlarmDefinitionVoPage = new Page<>();
        monitorAlarmDefinitionVoPage.setRecords(monitorAlarmDefinitionVos);
        monitorAlarmDefinitionVoPage.setTotal(monitorAlarmDefinitionPage.getTotal());
        return monitorAlarmDefinitionVoPage;
    }
}
