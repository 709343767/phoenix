package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.*;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorAlarmRecordDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmRecord;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorAlarmRecordService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeAlarmRecordVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorAlarmRecordVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gitee.pifeng.monitoring.common.util.CollectionUtils.distinctByKey;

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
        Map<String, Object> map = this.monitorAlarmRecordDao.getAlarmRecordSuccessRateStatistics();
        return HomeAlarmRecordVo.builder()
                .alarmRecordSum(NumberUtil.parseInt(map.get("alarmRecordSum").toString()))
                .alarmRecordSuccessSum(NumberUtil.parseInt(map.get("alarmRecordSuccessSum").toString()))
                .alarmRecordFailSum(NumberUtil.parseInt(map.get("alarmRecordFailSum").toString()))
                .alarmRecordUnsentSum(NumberUtil.parseInt(map.get("alarmRecordUnsentSum").toString()))
                .alarmSucRate(NumberUtil.round(map.get("alarmSucRate").toString(), 2).toString())
                .build();
    }

    /**
     * <p>
     * 获取监控告警列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param type       告警类型
     * @param level      告警级别
     * @param status     告警状态
     * @param title      告警标题
     * @param content    告警内容
     * @param number     被告警人号码
     * @param insertDate 记录日期
     * @param updateDate 告警日期
     * @return 分页Page对象
     * @author 皮锋
     * @custom.date 2020/8/3 11:07
     */
    @Override
    public Page<MonitorAlarmRecordVo> getMonitorAlarmRecordList(Long current, Long size, String type, String level,
                                                                String status, String title, String content,
                                                                String number, String insertDate, String updateDate) {
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
        if (StringUtils.equals(ZeroOrOneConstants.MINUS_ONE, status)) {
            // 自己用-1代表未发送
            lambdaQueryWrapper.isNull(MonitorAlarmRecord::getStatus);
        } else {
            if (StringUtils.isNotBlank(status)) {
                lambdaQueryWrapper.eq(MonitorAlarmRecord::getStatus, status);
            }
        }
        if (StringUtils.isNotBlank(title)) {
            lambdaQueryWrapper.like(MonitorAlarmRecord::getTitle, title);
        }
        if (StringUtils.isNotBlank(content)) {
            lambdaQueryWrapper.like(MonitorAlarmRecord::getContent, content);
        }
        if (StringUtils.isNotBlank(number)) {
            lambdaQueryWrapper.like(MonitorAlarmRecord::getNumber, number);
        }
        if (StringUtils.isNotBlank(insertDate)) {
            Date startDateTime = DateTimeUtils.string2Date(insertDate, DateTimeStylesEnums.YYYY_MM_DD);
            Date endDateTime = DateUtil.endOfDay(startDateTime).toJdkDate();
            lambdaQueryWrapper.between(MonitorAlarmRecord::getInsertTime, startDateTime, endDateTime);
        }
        if (StringUtils.isNotBlank(updateDate)) {
            Date startDateTime = DateTimeUtils.string2Date(updateDate, DateTimeStylesEnums.YYYY_MM_DD);
            Date endDateTime = DateUtil.endOfDay(startDateTime).toJdkDate();
            lambdaQueryWrapper.between(MonitorAlarmRecord::getUpdateTime, startDateTime, endDateTime);
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
     * 获取监控告警列表
     * </p>
     *
     * @param type    告警类型
     * @param level   告警级别
     * @param status  告警状态
     * @param title   告警标题
     * @param content 告警内容
     * @return 监控告警表现层对象列表
     * @author 皮锋
     * @custom.date 2021/5/18 22:50
     */
    @Override
    public List<MonitorAlarmRecordVo> getMonitorAlarmRecordList(String type, String level, String status, String title, String content) {
        LambdaQueryWrapper<MonitorAlarmRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 倒叙查询
        lambdaQueryWrapper.orderByDesc(MonitorAlarmRecord::getInsertTime);
        if (StringUtils.isNotBlank(type)) {
            lambdaQueryWrapper.eq(MonitorAlarmRecord::getType, type);
        }
        if (StringUtils.isNotBlank(level)) {
            lambdaQueryWrapper.eq(MonitorAlarmRecord::getLevel, level);
        }
        if (StringUtils.equals(ZeroOrOneConstants.MINUS_ONE, status)) {
            // 自己用-1代表未发送
            lambdaQueryWrapper.isNull(MonitorAlarmRecord::getStatus);
        } else {
            if (StringUtils.isNotBlank(status)) {
                lambdaQueryWrapper.eq(MonitorAlarmRecord::getStatus, status);
            }
        }
        if (StringUtils.isNotBlank(title)) {
            lambdaQueryWrapper.like(MonitorAlarmRecord::getTitle, title);
        }
        if (StringUtils.isNotBlank(content)) {
            lambdaQueryWrapper.like(MonitorAlarmRecord::getContent, content);
        }
        List<MonitorAlarmRecord> monitorAlarmRecords = this.monitorAlarmRecordDao.selectList(lambdaQueryWrapper);
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

    /**
     * <p>
     * 获取最近7天的告警统计信息
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/18 10:25
     */
    @Override
    public LayUiAdminResultVo getLast7DaysAlarmRecordStatistics() {
        List<Map<String, Object>> maps = this.monitorAlarmRecordDao.getLast7DaysAlarmRecordStatistics();
        return LayUiAdminResultVo.ok(maps);
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
        List<Map<String, Object>> maps = this.monitorAlarmRecordDao.getAlarmRecordTypeStatistics();
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
    public MonitorAlarmRecordVo monitorAlarmRecordDetail(Long id) {
        MonitorAlarmRecord monitorAlarmRecord = this.monitorAlarmRecordDao.selectById(id);
        MonitorAlarmRecordVo monitorAlarmRecordVo = MonitorAlarmRecordVo.builder().build().convertFor(monitorAlarmRecord);
        // 告警类型（SERVER、NET、TCPIP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、CUSTOM）
        String type = monitorAlarmRecordVo.getType();
        if (StringUtils.equals(MonitorTypeEnums.SERVER.name(), type)) {
            monitorAlarmRecordVo.setType("服务器");
        }
        if (StringUtils.equals(MonitorTypeEnums.NET.name(), type)) {
            monitorAlarmRecordVo.setType("网络");
        }
        if (StringUtils.equals(MonitorTypeEnums.TCPIP4SERVICE.name(), type)) {
            monitorAlarmRecordVo.setType("TCP/IP服务");
        }
        if (StringUtils.equals(MonitorTypeEnums.HTTP4SERVICE.name(), type)) {
            monitorAlarmRecordVo.setType("HTTP服务");
        }
        if (StringUtils.equals(MonitorTypeEnums.DOCKER.name(), type)) {
            monitorAlarmRecordVo.setType("Docker");
        }
        if (StringUtils.equals(MonitorTypeEnums.INSTANCE.name(), type)) {
            monitorAlarmRecordVo.setType("应用");
        }
        if (StringUtils.equals(MonitorTypeEnums.CUSTOM.name(), type)) {
            monitorAlarmRecordVo.setType("自定义");
        }
        if (StringUtils.equals(MonitorTypeEnums.DATABASE.name(), type)) {
            monitorAlarmRecordVo.setType("数据库");
        }
        // 告警方式（SMS、MAIL、...）
        String way = monitorAlarmRecordVo.getWay();
        monitorAlarmRecordVo.setWay(StringUtils.replaceEach(way,
                new String[]{AlarmWayEnums.SMS.name(), AlarmWayEnums.MAIL.name()},
                new String[]{"短信", "邮件"}));
        // 告警级别（INFO、WARM、ERROR、FATAL）
        String level = monitorAlarmRecordVo.getLevel();
        monitorAlarmRecordVo.setLevel(StringUtils.replaceEach(level,
                new String[]{AlarmLevelEnums.INFO.name(), AlarmLevelEnums.WARN.name(), AlarmLevelEnums.ERROR.name(), AlarmLevelEnums.FATAL.name()},
                new String[]{"消息", "警告", "错误", "严重"}));
        // 告警发送状态（0：失败；1：成功）
        String status = monitorAlarmRecordVo.getStatus();
        monitorAlarmRecordVo.setStatus(StringUtils.replaceEach(status,
                new String[]{ZeroOrOneConstants.ZERO, ZeroOrOneConstants.ONE},
                new String[]{"失败", "成功"}));
        if (StringUtils.isBlank(monitorAlarmRecordVo.getStatus())) {
            monitorAlarmRecordVo.setStatus("不提醒");
        }
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
        List<MonitorAlarmRecordVo> monitorAlarmRecordVos = this.getMonitorAlarmRecordList(1L, 20L, null, null, null, null, null, null, null, null).getRecords();
        if (CollectionUtils.isNotEmpty(monitorAlarmRecordVos)) {
            // 根据 code（告警代码） 去重重复值
            monitorAlarmRecordVos = monitorAlarmRecordVos.stream().filter(distinctByKey(MonitorAlarmRecordVo::getCode)).collect(Collectors.toList());
            // 取出前5条
            if (monitorAlarmRecordVos.size() > 5) {
                // 左闭右开(包括fromIndex元素,不包括toIndex)
                monitorAlarmRecordVos = monitorAlarmRecordVos.subList(0, 5);
            }
        }
        return LayUiAdminResultVo.ok(monitorAlarmRecordVos);
    }

}
