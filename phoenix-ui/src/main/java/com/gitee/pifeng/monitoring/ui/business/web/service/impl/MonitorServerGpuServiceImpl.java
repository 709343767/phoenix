package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.util.DataSizeUtils;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorServerGpuDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerGpu;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerGpuService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerGpuVo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务器GPU服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025-06-09
 */
@Service
public class MonitorServerGpuServiceImpl extends ServiceImpl<IMonitorServerGpuDao, MonitorServerGpu> implements IMonitorServerGpuService {

    /**
     * <p>
     * 获取服务器详情页面服务器GPU信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器详情页面服务器GPU信息表现层对象
     * @author 皮锋
     * @custom.date 2025/6/16 11:38
     */
    @Override
    public List<ServerDetailPageServerGpuVo> getServerDetailPageServerGpuInfo(String ip) {
        LambdaQueryWrapper<MonitorServerGpu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerGpu::getIp, ip);
        List<MonitorServerGpu> monitorServerGpus = this.baseMapper.selectList(lambdaQueryWrapper);
        List<ServerDetailPageServerGpuVo> serverDetailPageServerGpuVos = Lists.newLinkedList();
        for (MonitorServerGpu monitorServerGpu : monitorServerGpus) {
            ServerDetailPageServerGpuVo serverDetailPageServerGpuVo = new ServerDetailPageServerGpuVo();
            serverDetailPageServerGpuVo.setIp(monitorServerGpu.getIp());
            serverDetailPageServerGpuVo.setGpuNo(monitorServerGpu.getGpuNo());
            serverDetailPageServerGpuVo.setGpuName(monitorServerGpu.getGpuName());
            serverDetailPageServerGpuVo.setGpuDeviceId(monitorServerGpu.getGpuDeviceId());
            serverDetailPageServerGpuVo.setGpuVendor(monitorServerGpu.getGpuVendor());
            serverDetailPageServerGpuVo.setGpuVersionInfo(monitorServerGpu.getGpuVersionInfo());
            serverDetailPageServerGpuVo.setGpuVramTotal(DataSizeUtils.format(monitorServerGpu.getGpuVramTotal()));
            serverDetailPageServerGpuVos.add(serverDetailPageServerGpuVo);
        }
        return serverDetailPageServerGpuVos;
    }

}
