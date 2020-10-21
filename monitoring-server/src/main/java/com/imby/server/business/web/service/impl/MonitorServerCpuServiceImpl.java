package com.imby.server.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.server.business.web.dao.IMonitorServerCpuDao;
import com.imby.server.business.web.entity.MonitorServerCpu;
import com.imby.server.business.web.service.IMonitorServerCpuService;
import com.imby.server.business.web.vo.ServerDetailPageServerCpuVo;
import com.imby.server.core.CalculateDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param ip   服务器IP地址
     * @param time 时间
     * @return 服务器详情页面服务器CPU信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/19 14:22
     */
    @Override
    public List<ServerDetailPageServerCpuVo> getServerDetailPageServerCpu(String ip, String time) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("ip", ip);
        // 计算时间
        CalculateDateTime calculateDateTime = new CalculateDateTime(time).invoke();
        // 开始时间
        Date startTime = calculateDateTime.getStartTime();
        // 结束时间
        Date endTime = calculateDateTime.getEndTime();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<ServerDetailPageServerCpuVo> serverDetailPageServerCpuVos = this.monitorServerCpuDao.getServerDetailPageServerCpu(params);
        for (ServerDetailPageServerCpuVo serverDetailPageServerCpuVo : serverDetailPageServerCpuVos) {
            // 乘以100，保留两位小数
            serverDetailPageServerCpuVo.setCpuCombined(NumberUtil.round(serverDetailPageServerCpuVo.getCpuCombined() * 100D, 2).doubleValue());
        }
        return serverDetailPageServerCpuVos;
    }
}
