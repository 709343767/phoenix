package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorLogExceptionDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogException;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogExceptionService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogExceptionVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private IMonitorLogExceptionDao monitorLogExceptionDao;

    /**
     * <p>
     * 获取异常日志列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param excName    异常名称
     * @param excMessage 异常信息
     * @param insertTime 插入时间
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/6/18 9:01
     */
    @Override
    public Page<MonitorLogExceptionVo> getMonitorLogExceptionList(Long current, Long size, String excName, String excMessage, String insertTime) {
        // 查询数据库
        IPage<MonitorLogException> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorLogException> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 只查询部分字段
        lambdaQueryWrapper.select(MonitorLogException::getId, MonitorLogException::getOperMethod,
                MonitorLogException::getExcName, MonitorLogException::getExcMessage,
                MonitorLogException::getUri, MonitorLogException::getIp,
                MonitorLogException::getUsername, MonitorLogException::getInsertTime);
        if (StringUtils.isNotBlank(excName)) {
            lambdaQueryWrapper.like(MonitorLogException::getExcName, excName);
        }
        if (StringUtils.isNotBlank(excMessage)) {
            lambdaQueryWrapper.like(MonitorLogException::getExcMessage, excMessage);
        }
        if (StringUtils.isNotBlank(insertTime)) {
            String[] split = StringUtils.split(insertTime, "~");
            lambdaQueryWrapper.between(MonitorLogException::getInsertTime, DateTimeUtils.string2Date(split[0].trim(), DateTimeStylesEnums.YYYY_MM_DD), DateTimeUtils.string2Date(split[1].trim(), DateTimeStylesEnums.YYYY_MM_DD));
        }
        // 时间倒序
        lambdaQueryWrapper.orderByDesc(MonitorLogException::getInsertTime);
        IPage<MonitorLogException> monitorLogExceptionPage = this.monitorLogExceptionDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorLogException> monitorLogExceptions = monitorLogExceptionPage.getRecords();
        // 转换成异常日志表现层对象
        List<MonitorLogExceptionVo> monitorLogExceptionVos = Lists.newLinkedList();
        for (MonitorLogException monitorLogException : monitorLogExceptions) {
            MonitorLogExceptionVo monitorLogExceptionVo = MonitorLogExceptionVo.builder().build().convertFor(monitorLogException);
            monitorLogExceptionVos.add(monitorLogExceptionVo);
        }
        // 设置返回对象
        Page<MonitorLogExceptionVo> monitorLogExceptionVoPage = new Page<>();
        monitorLogExceptionVoPage.setRecords(monitorLogExceptionVos);
        monitorLogExceptionVoPage.setTotal(monitorLogExceptionPage.getTotal());
        return monitorLogExceptionVoPage;
    }

    /**
     * <p>
     * 删除异常日志
     * </p>
     *
     * @param monitorLogExceptionVos 异常日志信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/6/18 12:37
     */
    @Override
    public LayUiAdminResultVo deleteMonitorLogException(List<MonitorLogExceptionVo> monitorLogExceptionVos) {
        List<Long> ids = Lists.newArrayList();
        for (MonitorLogExceptionVo monitorLogExceptionVo : monitorLogExceptionVos) {
            ids.add(monitorLogExceptionVo.getId());
        }
        this.monitorLogExceptionDao.deleteBatchIds(ids);
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
        MonitorLogException monitorLogException = this.monitorLogExceptionDao.selectById(id);
        MonitorLogExceptionVo monitorLogExceptionVo = MonitorLogExceptionVo.builder().build().convertFor(monitorLogException);
        monitorLogExceptionVo.setReqParam(JSONUtil.formatJsonStr(monitorLogExceptionVo.getReqParam()));
        return monitorLogExceptionVo;
    }
}
