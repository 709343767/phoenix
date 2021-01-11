package com.gitee.pifeng.server.business.web.service.impl;

import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.server.business.web.dao.IMonitorServerNetcardDao;
import com.gitee.pifeng.server.business.web.entity.MonitorServerNetcard;
import com.gitee.pifeng.server.business.web.service.IMonitorServerNetcardService;
import com.gitee.pifeng.server.business.web.vo.ServerDetailPageServerNetcardVo;
import com.gitee.pifeng.server.business.web.vo.ServerDetailPageServerNetworkSpeedChartVo;
import com.gitee.pifeng.server.core.CalculateDateTime;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器网卡服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Service
public class MonitorServerNetcardServiceImpl extends ServiceImpl<IMonitorServerNetcardDao, MonitorServerNetcard> implements IMonitorServerNetcardService {

    /**
     * 服务器网卡数据访问对象
     */
    @Autowired
    private IMonitorServerNetcardDao monitorServerNetcardDao;

    /**
     * <p>
     * 获取服务器详情页面服务器网卡信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器详情页面服务器网卡信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/30 19:49
     */
    @Override
    public List<ServerDetailPageServerNetcardVo> getServerDetailPageServerNetcardInfo(String ip) {
        List<MonitorServerNetcard> monitorServerNetcards = this.monitorServerNetcardDao.getServerDetailPageServerNetcardInfo(ip);
        List<ServerDetailPageServerNetcardVo> serverDetailPageServerNetcardVos = Lists.newLinkedList();
        for (MonitorServerNetcard monitorServerNetcard : monitorServerNetcards) {
            ServerDetailPageServerNetcardVo serverDetailPageServerNetcardVo = new ServerDetailPageServerNetcardVo();
            serverDetailPageServerNetcardVo.setId(monitorServerNetcard.getId());
            serverDetailPageServerNetcardVo.setIp(monitorServerNetcard.getIp());
            serverDetailPageServerNetcardVo.setNetNo(monitorServerNetcard.getNetNo());
            serverDetailPageServerNetcardVo.setName(monitorServerNetcard.getName());
            serverDetailPageServerNetcardVo.setType(monitorServerNetcard.getType());
            serverDetailPageServerNetcardVo.setAddress(monitorServerNetcard.getAddress());
            serverDetailPageServerNetcardVo.setMask(monitorServerNetcard.getMask());
            serverDetailPageServerNetcardVo.setBroadcast(monitorServerNetcard.getBroadcast());
            serverDetailPageServerNetcardVo.setHwAddr(monitorServerNetcard.getHwAddr());
            serverDetailPageServerNetcardVo.setDescription(monitorServerNetcard.getDescription());
            serverDetailPageServerNetcardVo.setInsertTime(monitorServerNetcard.getInsertTime());
            serverDetailPageServerNetcardVo.setUpdateTime(monitorServerNetcard.getUpdateTime());
            serverDetailPageServerNetcardVo.setRx(DataSizeUtil.format(monitorServerNetcard.getRxBytes()));
            serverDetailPageServerNetcardVo.setRxPackets(monitorServerNetcard.getRxPackets());
            serverDetailPageServerNetcardVo.setRxErrors(monitorServerNetcard.getRxErrors());
            serverDetailPageServerNetcardVo.setRxDropped(monitorServerNetcard.getRxDropped());
            serverDetailPageServerNetcardVo.setTx(DataSizeUtil.format(monitorServerNetcard.getTxBytes()));
            serverDetailPageServerNetcardVo.setTxPackets(monitorServerNetcard.getTxPackets());
            serverDetailPageServerNetcardVo.setTxErrors(monitorServerNetcard.getTxErrors());
            serverDetailPageServerNetcardVo.setTxDropped(monitorServerNetcard.getTxDropped());
            String downloadSpeed = DataSizeUtil.format(monitorServerNetcard.getDownloadBps().longValue());
            String uploadSpeed = DataSizeUtil.format(monitorServerNetcard.getUploadBps().longValue());
            serverDetailPageServerNetcardVo.setDownloadSpeed(ZeroOrOneConstants.ZERO.equals(downloadSpeed) ? ZeroOrOneConstants.ZERO : downloadSpeed + "/s");
            serverDetailPageServerNetcardVo.setUploadSpeed(ZeroOrOneConstants.ZERO.equals(uploadSpeed) ? ZeroOrOneConstants.ZERO : uploadSpeed + "/s");
            serverDetailPageServerNetcardVos.add(serverDetailPageServerNetcardVo);
        }
        return serverDetailPageServerNetcardVos;
    }

    /**
     * <p>
     * 获取服务器详情页面服务器网速图表信息
     * </p>
     *
     * @param ip   服务器IP地址
     * @param time 时间
     * @return 服务器详情页面服务器网速图表信息表现层对象
     * @author 皮锋
     * @custom.date 2021/1/10 20:44
     */
    @Override
    public List<ServerDetailPageServerNetworkSpeedChartVo> getServerDetailPageServerNetworkSpeedChartInfo(String ip, String address, String time) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("ip", ip);
        params.put("address", address);
        // 计算时间
        CalculateDateTime calculateDateTime = new CalculateDateTime(time).invoke();
        // 开始时间
        Date startTime = calculateDateTime.getStartTime();
        // 结束时间
        Date endTime = calculateDateTime.getEndTime();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<ServerDetailPageServerNetworkSpeedChartVo> networkSpeedChartVos = this.monitorServerNetcardDao.getServerDetailPageServerNetworkSpeedChartInfo(params);
        for (ServerDetailPageServerNetworkSpeedChartVo networkSpeedChartVo : networkSpeedChartVos) {
            // B/s转KB/s
            networkSpeedChartVo.setDownloadSpeed(NumberUtil.round(networkSpeedChartVo.getDownloadSpeed() / 1024D, 2).doubleValue());
            networkSpeedChartVo.setUploadSpeed(NumberUtil.round(networkSpeedChartVo.getUploadSpeed() / 1024D, 2).doubleValue());
        }
        return networkSpeedChartVos;
    }

}
