package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttp;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeHttpVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorHttpVo;
import org.hyperic.sigar.SigarException;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * HTTP信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-11
 */
public interface IMonitorHttpService extends IService<MonitorHttp> {

    /**
     * <p>
     * 获取HTTP列表
     * </p>
     *
     * @param current        当前页
     * @param size           每页显示条数
     * @param hostnameSource 主机名（来源）
     * @param urlTarget      URL地址（目的地）
     * @param method         请求方法
     * @param status         状态
     * @param monitorEnv     监控环境
     * @param monitorGroup   监控分组
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/4/11 10:51
     */
    Page<MonitorHttpVo> getMonitorHttpList(Long current, Long size, String hostnameSource, String urlTarget,
                                           String method, Integer status, String monitorEnv, String monitorGroup);

    /**
     * <p>
     * 删除HTTP
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:44
     */
    LayUiAdminResultVo deleteMonitorHttp(List<Long> ids);

    /**
     * <p>
     * 添加HTTP信息
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 10:16
     */
    LayUiAdminResultVo addMonitorHttp(MonitorHttpVo monitorHttpVo);

    /**
     * <p>
     * 编辑HTTP信息
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 12:26
     */
    LayUiAdminResultVo editMonitorHttp(MonitorHttpVo monitorHttpVo);

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
     * 获取home页的HTTP信息
     * </p>
     *
     * @return home页的HTTP信息表现层对象
     * @author 皮锋
     * @custom.date 2022/4/23 13:23
     */
    HomeHttpVo getHomeHttpInfo();

    /**
     * <p>
     * 测试HTTP连通性
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：HTTP连通性
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/10/9 22:23
     */
    LayUiAdminResultVo testMonitorHttp(MonitorHttpVo monitorHttpVo) throws SigarException, IOException;

    /**
     * <p>
     * 根据主键ID获取HTTP信息
     * </p>
     *
     * @param id 主键ID
     * @return HTTP信息表现层对象
     * @author 皮锋
     * @custom.date 2024/09/27 21:34
     */
    MonitorHttpVo getMonitorHttpVoById(Long id);

}
