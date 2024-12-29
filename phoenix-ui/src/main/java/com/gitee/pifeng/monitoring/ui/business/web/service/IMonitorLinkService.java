package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLink;

import java.util.List;

/**
 * <p>
 * 链路 服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-12-19
 */
public interface IMonitorLinkService extends IService<MonitorLink> {

    /**
     * <p>
     * 删除链路信息
     * </p>
     *
     * @param nodes           节点列表
     * @param monitorTypeEnum 监控类型
     * @return 删除记录数
     * @author 皮锋
     * @custom.date 2023/8/11 17:11
     */
    int deleteMonitorLinks(List<String> nodes, MonitorTypeEnums monitorTypeEnum);

}
