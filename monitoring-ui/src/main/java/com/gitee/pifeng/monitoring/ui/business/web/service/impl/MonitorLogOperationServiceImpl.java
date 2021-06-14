package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorLogOperationDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogOperation;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogOperationService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorLogOperationVo;
import com.google.common.collect.Lists;
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
     * @param current 当前页
     * @param size    每页显示条数
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/6/14 21:28
     */
    @Override
    public Page<MonitorLogOperationVo> getMonitorLogOperationList(Long current, Long size) {
        // 查询数据库
        IPage<MonitorLogOperation> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorLogOperation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 只查询部分字段
        lambdaQueryWrapper.select(MonitorLogOperation::getId, MonitorLogOperation::getOperType, MonitorLogOperation::getOperMethod,
                MonitorLogOperation::getOperModule, MonitorLogOperation::getOperDesc, MonitorLogOperation::getUri,
                MonitorLogOperation::getUsername, MonitorLogOperation::getIp, MonitorLogOperation::getInsertTime);
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
}
