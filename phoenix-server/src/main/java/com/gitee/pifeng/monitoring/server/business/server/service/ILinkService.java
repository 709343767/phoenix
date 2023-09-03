package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorLink;

import java.util.List;

/**
 * <p>
 * 链路服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/12/18 17:46
 */
public interface ILinkService extends IService<MonitorLink> {

    /**
     * <p>
     * 查询链路列表
     * </p>
     *
     * @param rootNode 根节点
     * @param link     链路
     * @param type     链路类型
     * @return 链路列表
     * @author 皮锋
     * @custom.date 2022/12/18 20:09
     */
    List<MonitorLink> selectMonitorLinks(String rootNode, String link, String type);

}
