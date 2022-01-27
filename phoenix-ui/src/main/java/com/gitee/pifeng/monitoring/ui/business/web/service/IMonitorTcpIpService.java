package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpIp;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeTcpIpVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorTcpIpVo;

import java.util.List;

/**
 * <p>
 * TCP/IP信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-01-10
 */
public interface IMonitorTcpIpService extends IService<MonitorTcpIp> {

    /**
     * <p>
     * 获取TCP/IP列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param ipSource   IP地址（来源）
     * @param ipTarget   IP地址（目的地）
     * @param portTarget 目标端口
     * @param protocol   协议
     * @param status     状态（0：网络不通，1：网络正常）
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2022/1/11 9:33
     */
    Page<MonitorTcpIpVo> getMonitorTcpIpList(Long current, Long size, String ipSource, String ipTarget, Integer portTarget, String protocol, String status);

    /**
     * <p>
     * 删除TCP
     * </p>
     *
     * @param monitorTcpIpVos TCP/IP信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:45
     */
    LayUiAdminResultVo deleteMonitorTcpIp(List<MonitorTcpIpVo> monitorTcpIpVos);

    /**
     * <p>
     * 添加TCP/IP信息
     * </p>
     *
     * @param monitorTcpIpVo TCP/IP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 10:17
     */
    LayUiAdminResultVo addMonitorTcpIp(MonitorTcpIpVo monitorTcpIpVo);

    /**
     * <p>
     * 编辑TCP/IP信息
     * </p>
     *
     * @param monitorTcpIpVo TCP/IP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 14:10
     */
    LayUiAdminResultVo editMonitorTcpIp(MonitorTcpIpVo monitorTcpIpVo);

    /**
     * <p>
     * 获取home页的TCP/IP信息
     * </p>
     *
     * @return home页的TCP/IP信息表现层对象
     * @author 皮锋
     * @custom.date 2022/1/27 10:41
     */
    HomeTcpIpVo getHomeTcpIpInfo();
}
