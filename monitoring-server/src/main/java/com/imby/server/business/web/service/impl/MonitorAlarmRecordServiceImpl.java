package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.server.business.web.dao.IMonitorAlarmRecordDao;
import com.imby.server.business.web.entity.MonitorAlarmRecord;
import com.imby.server.business.web.service.IMonitorAlarmRecordService;
import com.imby.server.business.web.vo.HomeAlarmRecordVo;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorAlarmRecordVo;
import com.imby.server.constant.WebResponseConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 告警记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @since 2020-08-04
 */
@Service
public class MonitorAlarmRecordServiceImpl extends ServiceImpl<IMonitorAlarmRecordDao, MonitorAlarmRecord> implements IMonitorAlarmRecordService {

    /**
     * 告警记录数据访问对象
     */
    @Autowired
    private IMonitorAlarmRecordDao monitorAlarmRecordDao;

    /**
     * <p>
     * 获取home页的告警记录信息
     * </p>
     *
     * @return home页的告警记录表现层对象
     * @author 皮锋
     * @custom.date 2020/8/4 17:01
     */
    @Override
    public HomeAlarmRecordVo getHomeAlarmRecordInfo() {
        List<MonitorAlarmRecord> monitorAlarmRecords = this.monitorAlarmRecordDao.selectList(new LambdaQueryWrapper<>());
        HomeAlarmRecordVo homeAlarmRecordVo = new HomeAlarmRecordVo();
        homeAlarmRecordVo.setAlarmRecordSum(monitorAlarmRecords.size());
        // 告警成功
        homeAlarmRecordVo.setAlarmRecordSuccessSum((int) monitorAlarmRecords.stream().filter(e -> (
                // 没发短信 && 没发邮件
                (StringUtils.isBlank(e.getSmsStatus()) && StringUtils.isBlank(e.getMailStatus())) ||
                        // 没发邮件 && 短信发送成功
                        (StringUtils.isBlank(e.getMailStatus()) && StringUtils.equals(e.getSmsStatus(), ZeroOrOneConstants.ONE)) ||
                        // 没发短信 && 邮件发送成功
                        (StringUtils.isBlank(e.getSmsStatus()) && StringUtils.equals(e.getMailStatus(), ZeroOrOneConstants.ONE)) ||
                        // 短信和邮件都发送成功
                        (StringUtils.equals(e.getSmsStatus(), ZeroOrOneConstants.ONE) && StringUtils.equals(e.getMailStatus(), ZeroOrOneConstants.ONE))))
                .count());
        // 告警失败
        homeAlarmRecordVo.setAlarmRecordFailSum((int) monitorAlarmRecords.stream().filter(e -> (
                // 短信发送失败
                StringUtils.equals(e.getSmsStatus(), ZeroOrOneConstants.ZERO) ||
                        // 邮件发送失败
                        StringUtils.equals(e.getMailStatus(), ZeroOrOneConstants.ZERO)))
                .count());
        return homeAlarmRecordVo;
    }

    /**
     * <p>
     * 获取监控告警列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param type    告警类型
     * @param level   告警级别
     * @param title   告警标题
     * @param content 告警内容
     * @return 分页Page对象
     * @author 皮锋
     * @custom.date 2020/8/3 11:07
     */
    @Override
    public Page<MonitorAlarmRecordVo> getMonitorAlarmRecordList(Long current, Long size, String type, String level, String title, String content) {
        // 查询数据库
        IPage<MonitorAlarmRecord> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorAlarmRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 倒叙查询
        lambdaQueryWrapper.orderByDesc(MonitorAlarmRecord::getInsertTime);
        if (StringUtils.isNotBlank(type)) {
            lambdaQueryWrapper.eq(MonitorAlarmRecord::getType, type);
        }
        if (StringUtils.isNotBlank(level)) {
            lambdaQueryWrapper.eq(MonitorAlarmRecord::getLevel, level);
        }
        if (StringUtils.isNotBlank(title)) {
            lambdaQueryWrapper.like(MonitorAlarmRecord::getTitle, title);
        }
        if (StringUtils.isNotBlank(content)) {
            lambdaQueryWrapper.like(MonitorAlarmRecord::getContent, content);
        }
        IPage<MonitorAlarmRecord> monitorAlarmRecordPage = this.monitorAlarmRecordDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorAlarmRecord> monitorAlarmRecords = monitorAlarmRecordPage.getRecords();
        // 转换成监控告警表现层对象
        List<MonitorAlarmRecordVo> monitorAlarmRecordVos = Lists.newLinkedList();
        for (MonitorAlarmRecord monitorAlarmRecord : monitorAlarmRecords) {
            MonitorAlarmRecordVo monitorAlarmRecordVo = MonitorAlarmRecordVo.builder().build().convertFor(monitorAlarmRecord);
            monitorAlarmRecordVos.add(monitorAlarmRecordVo);
        }
        // 设置返回对象
        Page<MonitorAlarmRecordVo> monitorAlarmRecordVoPage = new Page<>();
        monitorAlarmRecordVoPage.setRecords(monitorAlarmRecordVos);
        monitorAlarmRecordVoPage.setTotal(monitorAlarmRecordPage.getTotal());
        return monitorAlarmRecordVoPage;
    }

    /**
     * <p>
     * 删除告警记录
     * </p>
     *
     * @param monitorAlarmRecordVos 告警记录
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 17:00
     */
    @Override
    public LayUiAdminResultVo deleteMonitorAlarmRecord(List<MonitorAlarmRecordVo> monitorAlarmRecordVos) {
        int size = monitorAlarmRecordVos.size();
        List<Long> ids = Lists.newLinkedList();
        for (MonitorAlarmRecordVo monitorAlarmRecordVo : monitorAlarmRecordVos) {
            ids.add(monitorAlarmRecordVo.getId());
        }
        int result = this.monitorAlarmRecordDao.deleteBatchIds(ids);
        if (result == size) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }
}
