package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorLogExceptionDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogException;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogExceptionService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogExceptionVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 异常日志服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Service
public class MonitorLogExceptionServiceImpl extends ServiceImpl<IMonitorLogExceptionDao, MonitorLogException> implements IMonitorLogExceptionService {

    /**
     * <p>
     * 获取异常日志列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param instanceId   应用实例ID
     * @param instanceName 应用实例名
     * @param excName      异常名称
     * @param excMessage   异常信息
     * @param operMethod   操作方法
     * @param uri          请求URI
     * @param ip           请求IP
     * @param insertTime   插入时间
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/6/18 9:01
     */
    @Override
    public Page<MonitorLogExceptionVo> getMonitorLogExceptionList(Long current, Long size, String instanceId, String instanceName, String excName, String excMessage, String operMethod, String uri, String ip, String insertTime) {
        // 查询数据库
        IPage<MonitorLogException> ipage = new Page<>(current, size);
        Date startDateTime = null;
        Date endDateTime = null;
        if (StringUtils.isNotBlank(insertTime)) {
            String[] split = StringUtils.split(insertTime, "~");
            startDateTime = DateTimeUtils.string2Date(split[0].trim(), DateTimeStylesEnums.YYYY_MM_DD);
            endDateTime = DateTimeUtils.string2Date(split[1].trim(), DateTimeStylesEnums.YYYY_MM_DD);
            endDateTime = DateUtil.endOfDay(endDateTime).toJdkDate();
        }
        IPage<MonitorLogExceptionVo> monitorLogExceptionPage = this.baseMapper.getMonitorLogExceptionList(ipage, instanceId, instanceName, excName, excMessage, operMethod, uri, ip, startDateTime, endDateTime);
        // 设置返回对象
        Page<MonitorLogExceptionVo> monitorLogExceptionVoPage = new Page<>();
        monitorLogExceptionVoPage.setRecords(monitorLogExceptionPage.getRecords());
        monitorLogExceptionVoPage.setTotal(monitorLogExceptionPage.getTotal());
        return monitorLogExceptionVoPage;
    }

    /**
     * <p>
     * 删除异常日志
     * </p>
     *
     * @param ids 异常日志ID列表
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/6/18 12:37
     */
    @Override
    public LayUiAdminResultVo deleteMonitorLogException(List<Long> ids) {
        this.baseMapper.deleteBatchIds(ids);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 清空异常日志
     * </p>
     *
     * @return layUiAdmin响应对象：如果清空成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/7/13 15:21
     */
    @Override
    public LayUiAdminResultVo cleanupMonitorLogException() {
        this.baseMapper.cleanupMonitorLogException();
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 获取异常日志信息
     * </p>
     *
     * @param id 异常日志ID
     * @return 异常日志表现层对象
     * @author 皮锋
     * @custom.date 2021/6/18 16:31
     */
    @Override
    public MonitorLogExceptionVo getMonitorLogExceptionInfo(Long id) {
        MonitorLogExceptionVo monitorLogExceptionVo = this.baseMapper.getMonitorLogExceptionById(id);
        String reqParam = monitorLogExceptionVo.getReqParam();
        monitorLogExceptionVo.setReqParam(reqParam != null ? JSONUtil.formatJsonStr(reqParam) : null);
        String isAlarm = monitorLogExceptionVo.getIsAlarm();
        monitorLogExceptionVo.setIsAlarm(StringUtils.replaceEach(isAlarm, new String[]{ZeroOrOneConstants.ZERO, ZeroOrOneConstants.ONE}, new String[]{"否", "是"}));
        if (StringUtils.isBlank(isAlarm)) {
            monitorLogExceptionVo.setIsAlarm("未知");
        }
        return monitorLogExceptionVo;
    }

}
