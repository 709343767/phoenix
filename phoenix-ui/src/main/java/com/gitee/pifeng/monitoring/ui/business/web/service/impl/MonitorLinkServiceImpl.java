package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.exception.DbException;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorLinkDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLink;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLinkService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 链路 服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-12-19
 */
@Service
public class MonitorLinkServiceImpl extends ServiceImpl<IMonitorLinkDao, MonitorLink> implements IMonitorLinkService {

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
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public int deleteMonitorLinks(List<String> nodes, MonitorTypeEnums monitorTypeEnum) {
        if (monitorTypeEnum == null) {
            throw new DbException("监控类型不能为空！");
        }
        // node列表为空的情况下，直接删除，不需要过多考虑
        if (CollectionUtils.isEmpty(nodes)) {
            LambdaUpdateWrapper<MonitorLink> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
            lambdaUpdateWrapper.eq(MonitorLink::getType, monitorTypeEnum.name());
            return this.baseMapper.delete(lambdaUpdateWrapper);
        }
        LambdaQueryWrapper<MonitorLink> monitorLinkLambdaQueryWrapper = Wrappers.lambdaQuery();
        monitorLinkLambdaQueryWrapper.eq(MonitorLink::getType, monitorTypeEnum.name());
        monitorLinkLambdaQueryWrapper.and(wrapper -> {
            wrapper.or(w -> w.in(MonitorLink::getRootNode, nodes));
            for (String node : nodes) {
                wrapper.or(w -> w.like(MonitorLink::getLink, node));
            }
            return wrapper;
        });
        List<MonitorLink> monitorLinks = this.baseMapper.selectList(monitorLinkLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(monitorLinks)) {
            return 0;
        }
        int resultCount = 0;
        // 要根据根节点删除的集合
        List<String> delRootNodes = Lists.newArrayList();
        // 要根据链路删除的节点集合
        List<String> delLinks = Lists.newArrayList();
        for (String node : nodes) {
            for (MonitorLink monitorLink : monitorLinks) {
                String rootNode = monitorLink.getRootNode();
                String link = monitorLink.getLink();
                if (StringUtils.equals(node, rootNode)) {
                    delRootNodes.add(rootNode);
                }
                if (StringUtils.equals(node, link)) {
                    delLinks.add(link);
                }
                // 更新链路
                if (StringUtils.contains(link, node) && !StringUtils.equals(node, link)) {
                    String[] links = StrUtil.split(link, ",");
                    int indexOf = ArrayUtil.indexOf(links, node);
                    String newLink = ArrayUtil.join(ArrayUtil.remove(links, indexOf), ",");
                    String[] linkTimes = StrUtil.split(monitorLink.getLinkTime(), ",");
                    String newLinkTime = ArrayUtil.join(ArrayUtil.remove(linkTimes, indexOf), ",");
                    monitorLink.setLink(newLink);
                    monitorLink.setLinkTime(newLinkTime);
                    // 移除完之后都没有了，那就得删除，否则是更新
                    if (StringUtils.isBlank(newLink)) {
                        resultCount += this.baseMapper.deleteById(monitorLink.getId());
                    } else {
                        boolean b = this.updateById(monitorLink);
                        if (b) {
                            resultCount += 1;
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(delRootNodes)) {
            LambdaUpdateWrapper<MonitorLink> delRootNodesLambdaUpdateWrapper = Wrappers.lambdaUpdate();
            delRootNodesLambdaUpdateWrapper.in(MonitorLink::getRootNode, delRootNodes);
            resultCount += this.baseMapper.delete(delRootNodesLambdaUpdateWrapper);
        }
        if (CollectionUtils.isNotEmpty(delLinks)) {
            LambdaUpdateWrapper<MonitorLink> delLinksLambdaUpdateWrapper = Wrappers.lambdaUpdate();
            delLinksLambdaUpdateWrapper.in(MonitorLink::getLink, delLinks);
            resultCount += this.baseMapper.delete(delLinksLambdaUpdateWrapper);
        }
        return resultCount;
    }

}
