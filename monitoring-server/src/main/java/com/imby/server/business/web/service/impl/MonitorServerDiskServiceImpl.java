package com.imby.server.business.web.service.impl;

import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imby.server.business.web.dao.IMonitorServerDiskDao;
import com.imby.server.business.web.entity.MonitorServerDisk;
import com.imby.server.business.web.service.IMonitorServerDiskService;
import com.imby.server.business.web.vo.ServerDetailPageServerDiskVo;
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
     * 获取服务器详情页面服务器磁盘信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器详情页面服务器磁盘信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/22 17:52
     */
    @Override
    public List<ServerDetailPageServerDiskVo> getServerDetailPageServerDisk(String ip) {
        List<MonitorServerDisk> monitorServerDisks = this.monitorServerDiskDao.getServerDetailPageServerDisk(ip);
        List<ServerDetailPageServerDiskVo> serverDetailPageServerDiskVos = Lists.newLinkedList();
        for (MonitorServerDisk monitorServerDisk : monitorServerDisks) {
            ServerDetailPageServerDiskVo serverDetailPageServerDiskVo = new ServerDetailPageServerDiskVo();
            serverDetailPageServerDiskVo.setDevName(monitorServerDisk.getDevName());
            serverDetailPageServerDiskVo.setTotalStr(DataSizeUtil.format(monitorServerDisk.getTotal()));
            serverDetailPageServerDiskVo.setFreeStr(DataSizeUtil.format(monitorServerDisk.getFree()));
            serverDetailPageServerDiskVo.setUsedStr(DataSizeUtil.format(monitorServerDisk.getUsed()));
            serverDetailPageServerDiskVo.setAvailStr(DataSizeUtil.format(monitorServerDisk.getAvail()));
            serverDetailPageServerDiskVo.setUsePercent(NumberUtil.round(monitorServerDisk.getUsePercent() * 100D, 2).doubleValue());
            serverDetailPageServerDiskVos.add(serverDetailPageServerDiskVo);
        }
        return serverDetailPageServerDiskVos;
    }

}
