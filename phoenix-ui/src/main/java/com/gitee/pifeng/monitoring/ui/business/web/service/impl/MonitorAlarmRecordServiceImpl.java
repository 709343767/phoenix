package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorAlarmRecordDao;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorAlarmRecordDetailDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmRecord;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmRecordDetail;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorAlarmRecordService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorAlarmRecordVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.util.HtmlUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 告警记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
@Service
public class MonitorAlarmRecordServiceImpl extends ServiceImpl<IMonitorAlarmRecordDao, MonitorAlarmRecord> implements IMonitorAlarmRecordService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 告警记录详情数据访问对象
     */
    @Autowired
    private IMonitorAlarmRecordDetailDao monitorAlarmRecordDetailDao;

    /**
     * <p>
     * 获取监控告警列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param type       告警类型
     * @param level      告警级别
     * @param title      告警标题
     * @param content    告警内容
     * @param insertDate 记录日期
     * @return 分页Page对象
     * @author 皮锋
     * @custom.date 2020/8/3 11:07
     */
    @Override
    public Page<MonitorAlarmRecordVo> getMonitorAlarmRecordList(Long current, Long size, String type, String level, String title,
                                                                String content, String insertDate) {
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
        if (StringUtils.isNotBlank(insertDate)) {
            Date startDateTime = DateTimeUtils.string2Date(insertDate, DateTimeStylesEnums.YYYY_MM_DD);
            Date endDateTime = DateUtil.endOfDay(startDateTime).toJdkDate();
            lambdaQueryWrapper.between(MonitorAlarmRecord::getInsertTime, startDateTime, endDateTime);
        }
        IPage<MonitorAlarmRecord> monitorAlarmRecordPage = this.baseMapper.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorAlarmRecord> monitorAlarmRecords = monitorAlarmRecordPage.getRecords();
        // 转换成监控告警表现层对象
        List<MonitorAlarmRecordVo> monitorAlarmRecordVos = Lists.newLinkedList();
        for (MonitorAlarmRecord monitorAlarmRecord : monitorAlarmRecords) {
            MonitorAlarmRecordVo monitorAlarmRecordVo = MonitorAlarmRecordVo.builder().build().convertFor(monitorAlarmRecord);
            // 告警内容
            monitorAlarmRecordVo.setContent(HtmlUtils.escapeHtmlButAllowBr(monitorAlarmRecordVo.getContent()));
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
     * 获取监控告警列表
     * </p>
     *
     * @param type       告警类型
     * @param level      告警级别
     * @param title      告警标题
     * @param content    告警内容
     * @param insertDate 记录日期
     * @return 监控告警表现层对象列表
     * @author 皮锋
     * @custom.date 2021/5/18 22:50
     */
    @Override
    public List<MonitorAlarmRecordVo> getMonitorAlarmRecordList(String type, String level, String title, String content, String insertDate) {
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
        if (StringUtils.isNotBlank(insertDate)) {
            Date startDateTime = DateTimeUtils.string2Date(insertDate, DateTimeStylesEnums.YYYY_MM_DD);
            Date endDateTime = DateUtil.endOfDay(startDateTime).toJdkDate();
            lambdaQueryWrapper.between(MonitorAlarmRecord::getInsertTime, startDateTime, endDateTime);
        }
        List<MonitorAlarmRecord> monitorAlarmRecords = this.baseMapper.selectList(lambdaQueryWrapper);
        // 转换成监控告警表现层对象
        List<MonitorAlarmRecordVo> monitorAlarmRecordVos = Lists.newLinkedList();
        for (MonitorAlarmRecord monitorAlarmRecord : monitorAlarmRecords) {
            MonitorAlarmRecordVo monitorAlarmRecordVo = MonitorAlarmRecordVo.builder().build().convertFor(monitorAlarmRecord);
            monitorAlarmRecordVos.add(monitorAlarmRecordVo);
        }
        return monitorAlarmRecordVos;
    }

    /**
     * <p>
     * 删除告警记录
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 17:00
     */
    @Retryable
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public LayUiAdminResultVo deleteMonitorAlarmRecord(List<Long> ids) {
        LambdaUpdateWrapper<MonitorAlarmRecordDetail> alarmRecordDetailUpdateWrapper = new LambdaUpdateWrapper<>();
        alarmRecordDetailUpdateWrapper.in(MonitorAlarmRecordDetail::getAlarmRecordId, ids);
        this.monitorAlarmRecordDetailDao.delete(alarmRecordDetailUpdateWrapper);
        this.baseMapper.deleteBatchIds(ids);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 清空告警记录
     * </p>
     *
     * @return layUiAdmin响应对象：如果清空成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/7/13 10:09
     */
    @Retryable
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public LayUiAdminResultVo cleanupMonitorAlarmRecord() {
        // 关闭外键检查
        this.jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        // 清空从表
        this.jdbcTemplate.execute("TRUNCATE TABLE MONITOR_ALARM_RECORD_DETAIL");
        // 清空主表
        this.jdbcTemplate.execute("TRUNCATE TABLE MONITOR_ALARM_RECORD");
        // 恢复外键检查
        this.jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 获取告警类型统计信息
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/23 9:52
     */
    @Override
    public LayUiAdminResultVo getAlarmRecordTypeStatistics() {
        List<Map<String, Object>> maps = this.baseMapper.getAlarmRecordTypeStatistics();
        // 总数
        long total = maps.stream().collect(Collectors.summarizingLong(e -> NumberUtil.parseLong(String.valueOf(e.get("totals"))))).getSum();
        for (Map<String, Object> map : maps) {
            long totals = NumberUtil.parseLong(String.valueOf(map.get("totals")));
            if (total != 0) {
                // 占比，四舍五入保留两位小数
                String rate = NumberUtil.formatPercent((double) totals / (double) total, 2);
                map.put("rate", rate);
            } else {
                map.put("rate", "100%");
            }
        }
        return LayUiAdminResultVo.ok(maps);
    }

    /**
     * <p>
     * 获取监控告警记录信息
     * </p>
     *
     * @param id 告警记录ID
     * @return 监控告警表现层对象
     * @author 皮锋
     * @custom.date 2021/6/18 22:15
     */
    @Override
    public MonitorAlarmRecordVo getMonitorAlarmRecord(Long id) {
        MonitorAlarmRecord monitorAlarmRecord = this.baseMapper.selectById(id);
        MonitorAlarmRecordVo monitorAlarmRecordVo = MonitorAlarmRecordVo.builder().build().convertFor(monitorAlarmRecord);
        // 告警类型（SERVER、NET、TCP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、NETWORK_DEVICE、CUSTOM）
        String type = monitorAlarmRecordVo.getType();
        monitorAlarmRecordVo.setType(StringUtils.replaceEach(type,
                new String[]{MonitorTypeEnums.SERVER.name(), MonitorTypeEnums.NET.name(),
                        MonitorTypeEnums.TCP4SERVICE.name(), MonitorTypeEnums.HTTP4SERVICE.name(),
                        MonitorTypeEnums.INSTANCE.name(), MonitorTypeEnums.CUSTOM.name(),
                        MonitorTypeEnums.DATABASE.name()},
                new String[]{"服务器", "网络", "TCP服务", "HTTP服务", "应用程序", "自定义", "数据库"}));
        // 告警级别（IGNORE、INFO、WARM、ERROR、FATAL）
        String level = monitorAlarmRecordVo.getLevel();
        monitorAlarmRecordVo.setLevel(StringUtils.replaceEach(level,
                new String[]{AlarmLevelEnums.IGNORE.name(), AlarmLevelEnums.INFO.name(), AlarmLevelEnums.WARN.name(), AlarmLevelEnums.ERROR.name(), AlarmLevelEnums.FATAL.name()},
                new String[]{"忽略", "消息", "警告", "错误", "严重"}));
        // 告警方式，多种方式用逗号分隔
        String way = monitorAlarmRecordVo.getWay();
        monitorAlarmRecordVo.setWay(StringUtils.isBlank(way) ? way : way.replace(",", "、")
                .replace(AlarmWayEnums.SMS.name(), "短信")
                .replace(AlarmWayEnums.MAIL.name(), "邮件"));
        // 告警内容
        String content = monitorAlarmRecordVo.getContent();
        monitorAlarmRecordVo.setContent(HtmlUtils.escapeHtmlButAllowBr(content));
        return monitorAlarmRecordVo;
    }

    /**
     * <p>
     * 获取最新的5条告警记录
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/9/1 9:30
     */
    @Override
    public LayUiAdminResultVo getLast5AlarmRecord() {
        Page<MonitorAlarmRecordVo> monitorAlarmRecordVoPage = this.getMonitorAlarmRecordList(1L, 5L, null, null, null, null, null);
        List<MonitorAlarmRecordVo> monitorAlarmRecordVos = monitorAlarmRecordVoPage.getRecords();
        return LayUiAdminResultVo.ok(monitorAlarmRecordVos);
    }

}
