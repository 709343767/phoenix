package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorServerNetcardHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerNetcardHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerNetcardHistoryService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.ServerDetailPageServerNetworkSpeedChartVo;
import com.gitee.pifeng.monitoring.ui.core.CalculateDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器网卡历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-25
 */
@Service
public class MonitorServerNetcardHistoryServiceImpl extends ServiceImpl<IMonitorServerNetcardHistoryDao, MonitorServerNetcardHistory> implements IMonitorServerNetcardHistoryService {

    /**
     * 服务器网卡历史记录数据访问对象
     */
    @Autowired
    private IMonitorServerNetcardHistoryDao monitorServerNetcardHistoryDao;

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
        List<ServerDetailPageServerNetworkSpeedChartVo> networkSpeedChartVos = this.monitorServerNetcardHistoryDao.getServerDetailPageServerNetworkSpeedChartInfo(params);
        for (ServerDetailPageServerNetworkSpeedChartVo networkSpeedChartVo : networkSpeedChartVos) {
            // B/s转KB/s
            networkSpeedChartVo.setDownloadSpeed(NumberUtil.round(networkSpeedChartVo.getDownloadSpeed() / 1024D, 2).doubleValue());
            networkSpeedChartVo.setUploadSpeed(NumberUtil.round(networkSpeedChartVo.getUploadSpeed() / 1024D, 2).doubleValue());
        }
        return networkSpeedChartVos;
    }

}
