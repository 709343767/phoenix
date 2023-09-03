package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorLinkDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorLink;
import com.gitee.pifeng.monitoring.server.business.server.service.ILinkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 链路服务接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/12/18 17:48
 */
@Service
public class LinkServiceImpl extends ServiceImpl<IMonitorLinkDao, MonitorLink> implements ILinkService {

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
    @Override
    public List<MonitorLink> selectMonitorLinks(String rootNode, String link, String type) {
        LambdaQueryWrapper<MonitorLink> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorLink::getRootNode, rootNode);
        lambdaQueryWrapper.eq(MonitorLink::getLink, link);
        lambdaQueryWrapper.eq(MonitorLink::getType, type);
        return this.baseMapper.selectList(lambdaQueryWrapper);
    }

}
