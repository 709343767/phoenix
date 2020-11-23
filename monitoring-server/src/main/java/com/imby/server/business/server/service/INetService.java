package com.imby.server.business.server.service;

import com.imby.server.business.server.entity.MonitorNet;

import java.util.List;

/**
 * <p>
 * 网络信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/31 17:20
 */
public interface INetService {

    /**
     * <p>
     * 获取网络信息列表
     * </p>
     *
     * @param ipSource IP地址（来源）
     * @return 网络信息列表
     * @author 皮锋
     * @custom.date 2020/11/23 9:09
     */
    List<MonitorNet> getMonitorNetList(String ipSource);

    /**
     * <p>
     * 更新网络信息
     * </p>
     *
     * @param monitorNet 网络信息
     * @return 更新记录数
     * @author 皮锋
     * @custom.date 2020/11/23 11:05
     */
    int updateMonitorNet(MonitorNet monitorNet);

}
