package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imby.common.constant.DateTimeStylesEnums;
import com.imby.common.util.DataSizeUtils;
import com.imby.common.util.DateTimeUtils;
import com.imby.server.business.web.dao.IMonitorJvmMemoryDao;
import com.imby.server.business.web.entity.MonitorJvmMemory;
import com.imby.server.business.web.service.IMonitorJvmMemoryService;
import com.imby.server.business.web.vo.MonitorJvmMemoryVo;
import com.imby.server.constant.TimeSelectConstants;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * java虚拟机内存信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Service
public class MonitorJvmMemoryServiceImpl extends ServiceImpl<IMonitorJvmMemoryDao, MonitorJvmMemory> implements IMonitorJvmMemoryService {

    /**
     * java虚拟机内存信息数据访问对象
     */
    @Autowired
    private IMonitorJvmMemoryDao monitorJvmMemoryDao;

    /**
     * <p>
     * 获取java虚拟机内存信息
     * </p>
     *
     * @param instanceId 应用实例ID
     * @param memoryType 内存类型
     * @param time       时间
     * @return java虚拟机内存信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/14 12:02
     */
    @Override
    public List<MonitorJvmMemoryVo> getJvmMemoryInfo(String instanceId, String memoryType, String time) {
        LambdaQueryWrapper<MonitorJvmMemory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorJvmMemory::getInstanceId, instanceId);
        if (StringUtils.isNotBlank(memoryType)) {
            lambdaQueryWrapper.eq(MonitorJvmMemory::getMemoryType, memoryType);
        }
        if (StringUtils.isNotBlank(time)) {
            // 开始时间
            Date startTime;
            // 结束时间
            Date endTime;
            switch (time) {
                case TimeSelectConstants.HOUR:
                    startTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                            .parseDateTime(DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd HH:00:00"))
                            .toDate();
                    endTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                            .parseDateTime(DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd HH:59:59"))
                            .toDate();
                    lambdaQueryWrapper.between(MonitorJvmMemory::getInsertTime, startTime, endTime);
                    break;
                case TimeSelectConstants.WEEK:
                    startTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                            .parseDateTime(DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd 00:00:00"))
                            .plusDays(-6).toDate();
                    endTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                            .parseDateTime(DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd 23:59:59"))
                            .toDate();
                    lambdaQueryWrapper.between(MonitorJvmMemory::getInsertTime, startTime, endTime);
                    break;
                case TimeSelectConstants.MONTH:
                    startTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                            .parseDateTime(DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd 00:00:00"))
                            .plusMonths(-1).plusDays(1).toDate();
                    endTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                            .parseDateTime(DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd 23:59:59"))
                            .toDate();
                    lambdaQueryWrapper.between(MonitorJvmMemory::getInsertTime, startTime, endTime);
                    break;
                case TimeSelectConstants.HALF_YEAR:
                    startTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                            .parseDateTime(DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd 00:00:00"))
                            .plusMonths(-6).plusDays(1).toDate();
                    endTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                            .parseDateTime(DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd 23:59:59"))
                            .toDate();
                    lambdaQueryWrapper.between(MonitorJvmMemory::getInsertTime, startTime, endTime);
                    break;
                case TimeSelectConstants.YEAR:
                    startTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                            .parseDateTime(DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd 00:00:00"))
                            .plusMonths(-12).plusDays(1).toDate();
                    endTime = DateTimeFormat.forPattern(DateTimeStylesEnums.YYYY_MM_DD_HH_MM_SS.getValue())
                            .parseDateTime(DateTimeUtils.dateToString(new Date(), "yyyy-MM-dd 23:59:59"))
                            .toDate();
                    lambdaQueryWrapper.between(MonitorJvmMemory::getInsertTime, startTime, endTime);
                    break;
                default:
                    break;
            }
        }
        List<MonitorJvmMemory> monitorJvmMemories = this.monitorJvmMemoryDao.selectList(lambdaQueryWrapper);
        List<MonitorJvmMemoryVo> monitorJvmMemoryVos = Lists.newLinkedList();
        for (MonitorJvmMemory monitorJvmMemory : monitorJvmMemories) {
            // 转MB
            monitorJvmMemory.setUsed(String.valueOf(DataSizeUtils.parse(monitorJvmMemory.getUsed()) / 1024 / 1024));
            // 转MB
            monitorJvmMemory.setCommitted(String.valueOf(DataSizeUtils.parse(monitorJvmMemory.getCommitted()) / 1024 / 1024));
            MonitorJvmMemoryVo monitorJvmMemoryVo = MonitorJvmMemoryVo.builder().build().convertFor(monitorJvmMemory);
            monitorJvmMemoryVos.add(monitorJvmMemoryVo);
        }
        return monitorJvmMemoryVos;
    }

}
