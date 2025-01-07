package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcp;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeTcpVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorTcpVo;
import org.hyperic.sigar.SigarException;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * TCP信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-01-10
 */
public interface IMonitorTcpService extends IService<MonitorTcp> {

    /**
     * <p>
     * 获取TCP列表
     * </p>
     *
     * @param current        当前页
     * @param size           每页显示条数
     * @param hostnameSource 主机名（来源）
     * @param hostnameTarget 主机名（目的地）
     * @param portTarget     目标端口
     * @param status         状态（0：网络不通，1：网络正常）
     * @param monitorEnv     监控环境
     * @param monitorGroup   监控分组
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2022/1/11 9:33
     */
    Page<MonitorTcpVo> getMonitorTcpList(Long current, Long size, String hostnameSource, String hostnameTarget,
                                         Integer portTarget, String status, String monitorEnv, String monitorGroup);

    /**
     * <p>
     * 删除TCP
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:45
     */
    LayUiAdminResultVo deleteMonitorTcp(List<Long> ids);

    /**
     * <p>
     * 添加TCP信息
     * </p>
     *
     * @param monitorTcpVo TCP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 10:17
     */
    LayUiAdminResultVo addMonitorTcp(MonitorTcpVo monitorTcpVo);

    /**
     * <p>
     * 编辑TCP信息
     * </p>
     *
     * @param monitorTcpVo TCP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 14:10
     */
    LayUiAdminResultVo editMonitorTcp(MonitorTcpVo monitorTcpVo);

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
     * 获取home页的TCP信息
     * </p>
     *
     * @return home页的TCP信息表现层对象
     * @author 皮锋
     * @custom.date 2022/1/27 10:41
     */
    HomeTcpVo getHomeTcpInfo();

    /**
     * <p>
     * 测试TCP连通性
     * </p>
     *
     * @param monitorTcpVo TCP信息表现层对象
     * @return layUiAdmin响应对象：TCP连通性
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/10/12 21:41
     */
    LayUiAdminResultVo testMonitorTcp(MonitorTcpVo monitorTcpVo) throws SigarException, IOException;

}
