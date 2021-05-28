package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorServerDiskDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerDisk;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerDiskService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerDiskChartVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务器磁盘服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Service
public class MonitorServerDiskServiceImpl extends ServiceImpl<IMonitorServerDiskDao, MonitorServerDisk> implements IMonitorServerDiskService {

    /**
     * 服务器磁盘数据访问对象
     */
    @Autowired
    private IMonitorServerDiskDao monitorServerDiskDao;

    /**
     * <p>
     * 获取服务器详情页面服务器磁盘图表信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器详情页面服务器磁盘图表信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/22 17:52
     */
    @Override
    public List<ServerDetailPageServerDiskChartVo> getServerDetailPageServerDiskChartInfo(String ip) {
        LambdaQueryWrapper<MonitorServerDisk> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorServerDisk::getIp, ip);
        List<MonitorServerDisk> monitorServerDisks = this.monitorServerDiskDao.selectList(lambdaQueryWrapper);
        List<ServerDetailPageServerDiskChartVo> serverDetailPageServerDiskChartVos = Lists.newLinkedList();
        for (MonitorServerDisk monitorServerDisk : monitorServerDisks) {
            ServerDetailPageServerDiskChartVo serverDetailPageServerDiskChartVo = new ServerDetailPageServerDiskChartVo();
            serverDetailPageServerDiskChartVo.setDevName(monitorServerDisk.getDevName());
            serverDetailPageServerDiskChartVo.setTotalStr(DataSizeUtil.format(monitorServerDisk.getTotal()));
            serverDetailPageServerDiskChartVo.setFreeStr(DataSizeUtil.format(monitorServerDisk.getFree()));
            serverDetailPageServerDiskChartVo.setUsedStr(DataSizeUtil.format(monitorServerDisk.getUsed()));
            serverDetailPageServerDiskChartVo.setAvailStr(DataSizeUtil.format(monitorServerDisk.getAvail()));
            serverDetailPageServerDiskChartVo.setUsePercent(NumberUtil.round(monitorServerDisk.getUsePercent() * 100D, 2).doubleValue());
            serverDetailPageServerDiskChartVos.add(serverDetailPageServerDiskChartVo);
        }
        return serverDetailPageServerDiskChartVos;
    }

}
