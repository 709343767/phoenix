package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorNet;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeNetVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorNetVo;
import org.hyperic.sigar.SigarException;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 网络信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/1 15:09
 */
public interface IMonitorNetService extends IService<MonitorNet> {

    /**
     * <p>
     * 获取home页的网络信息
     * </p>
     *
     * @return home页的网络信息表现层对象
     * @author 皮锋
     * @custom.date 2020/9/1 15:20
     */
    HomeNetVo getHomeNetInfo();

    /**
     * <p>
     * 获取网络列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param ipSource     IP地址（来源）
     * @param ipTarget     IP地址（目的地）
     * @param status       状态（0：网络不通，1：网络正常）
     * @param monitorEnv   监控环境
     * @param monitorGroup 监控分组
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2020/9/26 13:28
     */
    Page<MonitorNetVo> getMonitorNetList(Long current, Long size, String ipSource, String ipTarget, String status, String monitorEnv, String monitorGroup);

    /**
     * <p>
     * 删除网络
     * </p>
     *
     * @param monitorNetVos 网络信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/26 14:02
     */
    LayUiAdminResultVo deleteMonitorNet(List<MonitorNetVo> monitorNetVos);

    /**
     * <p>
     * 编辑网络信息
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/11/20 13:58
     */
    LayUiAdminResultVo editMonitorNetwork(MonitorNetVo monitorNetVo);

    /**
     * <p>
     * 添加网络信息
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/11/20 15:30
     */
    LayUiAdminResultVo addMonitorNetwork(MonitorNetVo monitorNetVo) throws NetException;

    /**
     * <p>
     * 设置是否开启监控（0：不开启监控；1：开启监控）
     * </p>
     *
     * @param id              主键ID
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:20
     */
    LayUiAdminResultVo setIsEnableMonitor(Long id, String isEnableMonitor);

    /**
     * <p>
     * 设置是否开启告警（0：不开启告警；1：开启告警）
     * </p>
     *
     * @param id            主键ID
     * @param isEnableAlarm 是否开启告警（0：不开启告警；1：开启告警）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:37
     */
    LayUiAdminResultVo setIsEnableAlarm(Long id, String isEnableAlarm);

    /**
     * <p>
     * 获取被监控网络源IP地址，获取失败则返回null。
     * </p>
     *
     * @return 被监控网络源IP地址
     * @author 皮锋
     * @custom.date 2021/10/6 22:19
     */
    String getSourceIp();

    /**
     * <p>
     * 测试网络连通性
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：网络连通性
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/10/9 10:16
     */
    LayUiAdminResultVo testMonitorNetwork(MonitorNetVo monitorNetVo) throws SigarException, IOException;

}
