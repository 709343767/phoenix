package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerSensors;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerSensorsVo;

/**
 * <p>
 * 服务器传感器服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-15
 */
public interface IMonitorServerSensorsService extends IService<MonitorServerSensors> {

    /**
     * <p>
     * 获取服务器详情页面服务器传感器信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器传感器信息表现层对象
     * @author 皮锋
     * @custom.date 2021/1/16 23:06
     */
    MonitorServerSensorsVo getServerDetailPageServerSensorsInfo(String ip);

    /**
     * <p>
     * 获取CPU平均温度
     * </p>
     *
     * @param ip 服务器IP地址
     * @return CPU平均温度
     * @author 皮锋
     * @custom.date 2021/1/17 21:02
     */
    Double getCpuTemperatureInfo(String ip);

}
