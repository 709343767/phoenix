package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorServerCpuDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerCpu;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerCpuService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerCpuVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务器CPU服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Service
public class MonitorServerCpuServiceImpl extends ServiceImpl<IMonitorServerCpuDao, MonitorServerCpu> implements IMonitorServerCpuService {

    /**
     * 服务器CPU数据访问对象
     */
    @Autowired
    private IMonitorServerCpuDao monitorServerCpuDao;

    /**
     * <p>
     * 获取服务器详情页面服务器CPU信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器CPU信息表现层对象
     * @author 皮锋
     * @custom.date 2020/11/1 14:35
     */
    @Override
    public List<MonitorServerCpuVo> getServerDetailPageServerCpuInfo(String ip) {
        LambdaQueryWrapper<MonitorServerCpu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerCpu::getIp, ip);
        List<MonitorServerCpu> monitorServerCpus = this.monitorServerCpuDao.selectList(lambdaQueryWrapper);
        List<MonitorServerCpuVo> monitorServerCpuVos = Lists.newLinkedList();
        for (MonitorServerCpu monitorServerCpu : monitorServerCpus) {
            MonitorServerCpuVo monitorServerCpuVo = MonitorServerCpuVo.builder().build().convertFor(monitorServerCpu);
            monitorServerCpuVos.add(monitorServerCpuVo);
        }
        return monitorServerCpuVos;
    }

}
