package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.DateTimeStylesEnums;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorLogOperationDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogOperation;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogOperationService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogOperationVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 操作日志服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Service
public class MonitorLogOperationServiceImpl extends ServiceImpl<IMonitorLogOperationDao, MonitorLogOperation> implements IMonitorLogOperationService {

    /**
     * 操作日志数据访问对象
     */
    @Autowired
    private IMonitorLogOperationDao monitorLogOperationDao;

    /**
     * <p>
     * 获取操作日志列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param operModule 功能模块
     * @param operDesc   操作描述
     * @param operType   操作类型
     * @param insertTime 插入时间
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/6/14 21:28
     */
    @Override
    public Page<MonitorLogOperationVo> getMonitorLogOperationList(Long current, Long size, String operModule, String operDesc, String operType, String insertTime) {
        // 查询数据库
        IPage<MonitorLogOperation> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorLogOperation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 只查询部分字段
        lambdaQueryWrapper.select(MonitorLogOperation::getId, MonitorLogOperation::getOperType, MonitorLogOperation::getOperMethod,
                MonitorLogOperation::getOperModule, MonitorLogOperation::getOperDesc, MonitorLogOperation::getUri,
                MonitorLogOperation::getUsername, MonitorLogOperation::getIp, MonitorLogOperation::getInsertTime);
        // 查询条件
        if (StringUtils.isNotBlank(operModule)) {
            lambdaQueryWrapper.like(MonitorLogOperation::getOperModule, operModule);
        }
        if (StringUtils.isNotBlank(operDesc)) {
            lambdaQueryWrapper.like(MonitorLogOperation::getOperDesc, operDesc);
        }
        if (StringUtils.isNotBlank(operType)) {
            lambdaQueryWrapper.eq(MonitorLogOperation::getOperType, operType);
        }
        if (StringUtils.isNotBlank(insertTime)) {
            String[] split = StringUtils.split(insertTime, "~");
            lambdaQueryWrapper.between(MonitorLogOperation::getInsertTime, DateTimeUtils.string2Date(split[0].trim(), DateTimeStylesEnums.YYYY_MM_DD), DateTimeUtils.string2Date(split[1].trim(), DateTimeStylesEnums.YYYY_MM_DD));
        }
        // 时间倒序
        lambdaQueryWrapper.orderByDesc(MonitorLogOperation::getInsertTime);
        IPage<MonitorLogOperation> monitorLogOperationPage = this.monitorLogOperationDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorLogOperation> monitorLogOperations = monitorLogOperationPage.getRecords();
        // 转换成操作日志表现层对象
        List<MonitorLogOperationVo> monitorLogOperationVos = Lists.newLinkedList();
        for (MonitorLogOperation monitorLogOperation : monitorLogOperations) {
            MonitorLogOperationVo monitorLogOperationVo = MonitorLogOperationVo.builder().build().convertFor(monitorLogOperation);
            monitorLogOperationVos.add(monitorLogOperationVo);
        }
        // 设置返回对象
        Page<MonitorLogOperationVo> monitorLogOperationVoPage = new Page<>();
        monitorLogOperationVoPage.setRecords(monitorLogOperationVos);
        monitorLogOperationVoPage.setTotal(monitorLogOperationPage.getTotal());
        return monitorLogOperationVoPage;
    }

    /**
     * <p>
     * 删除操作日志
     * </p>
     *
     * @param monitorLogOperationVos 操作日志信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/6/15 14:38
     */
    @Override
    public LayUiAdminResultVo deleteMonitorLogOperation(List<MonitorLogOperationVo> monitorLogOperationVos) {
        List<Long> ids = Lists.newArrayList();
        for (MonitorLogOperationVo monitorLogOperationVo : monitorLogOperationVos) {
            ids.add(monitorLogOperationVo.getId());
        }
        this.monitorLogOperationDao.deleteBatchIds(ids);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 获取操作日志信息
     * </p>
     *
     * @param id 操作日志ID
     * @return 操作日志表现层对象
     * @author 皮锋
     * @custom.date 2021/6/18 16:25
     */
    @Override
    public MonitorLogOperationVo getMonitorLogOperationInfo(Long id) {
        MonitorLogOperation monitorLogOperation = this.monitorLogOperationDao.selectById(id);
        MonitorLogOperationVo monitorLogOperationVo = MonitorLogOperationVo.builder().build().convertFor(monitorLogOperation);
        monitorLogOperationVo.setReqParam(monitorLogOperationVo.getReqParam() != null ? JSONUtil.formatJsonStr(monitorLogOperationVo.getReqParam()) : null);
        monitorLogOperationVo.setRespParam(monitorLogOperationVo.getRespParam() != null ? JSONUtil.formatJsonStr(monitorLogOperationVo.getRespParam()) : null);
        return monitorLogOperationVo;
    }
}
