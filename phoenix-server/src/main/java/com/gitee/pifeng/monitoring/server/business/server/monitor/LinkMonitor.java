package com.gitee.pifeng.monitoring.server.business.server.monitor;

import cn.hutool.core.util.ArrayUtil;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.dto.HeartbeatPackage;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.plug.core.InstanceGenerator;
import com.gitee.pifeng.monitoring.server.business.server.component.HeartbeatAspect;
import com.gitee.pifeng.monitoring.server.business.server.component.ServerAspect;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorLink;
import com.gitee.pifeng.monitoring.server.business.server.service.ILinkService;
import com.gitee.pifeng.monitoring.server.inf.ILinkListener;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 监控链路信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/6/1 10:24
 */
@Component
public class LinkMonitor implements ILinkListener {

    /**
     * 链路服务接口
     */
    @Autowired
    private ILinkService linkService;

    /**
     * <p>
     * 唤醒执行监控回调方法。
     * </p>
     * 此方法在{@link HeartbeatAspect#beforeWakeUp(JoinPoint)},{@link ServerAspect#beforeWakeUp(JoinPoint)}中被注册监听。
     *
     * @param param 回调参数
     * @author 皮锋
     * @custom.date 2020/3/30 20:18
     */
    @Override
    public void wakeUpMonitor(Object... param) {
        // 心跳包，包含应用程序链路信息
        if (param[0] instanceof HeartbeatPackage) {
            // 处理应用链路
            this.dealInstanceLink((HeartbeatPackage) param[0]);
        }
        // 服务器信息包，包含服务器链路信息
        else if (param[0] instanceof ServerPackage) {
            // 处理服务器链路
            this.dealServerLink((ServerPackage) param[0]);
        }
    }

    /**
     * <p>
     * 处理应用链路
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @author 皮锋
     * @custom.date 2023/6/1 10:39
     */
    private void dealInstanceLink(HeartbeatPackage heartbeatPackage) {
        synchronized (LinkMonitor.class) {
            // 服务端应用ID
            String instanceId = InstanceGenerator.getInstanceId();
            String link = ArrayUtil.join(ArrayUtil.reverse(heartbeatPackage.getChain().getInstanceChain().toArray(new String[]{})), ",");
            String rootNodeTime = String.valueOf(System.currentTimeMillis());
            String linkTime = ArrayUtil.join(ArrayUtil.reverse(heartbeatPackage.getChain().getTimeChain().toArray(new String[]{})), ",");
            // 自己，不处理
            if (StringUtils.equals(instanceId, link)) {
                return;
            }
            // 先查询有没有此链路
            List<MonitorLink> monitorLinks = this.linkService.selectMonitorLinks(instanceId, link, MonitorTypeEnums.INSTANCE.name());
            // 有——>更新
            if (CollectionUtils.isNotEmpty(monitorLinks)) {
                MonitorLink monitorLink = monitorLinks.get(0);
                monitorLink.setRootNodeTime(rootNodeTime);
                monitorLink.setLinkTime(linkTime);
                monitorLink.setUpdateTime(new Date());
                this.linkService.updateById(monitorLink);
            }
            // 没有——>插入
            else {
                MonitorLink monitorLink = MonitorLink.builder()
                        .rootNode(instanceId)
                        .rootNodeTime(rootNodeTime)
                        .link(link)
                        .linkTime(linkTime)
                        .type(MonitorTypeEnums.INSTANCE.name())
                        .insertTime(new Date())
                        .build();
                this.linkService.save(monitorLink);
            }
        }
    }

    /**
     * <p>
     * 处理服务器链路
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2023/6/1 10:40
     */
    private void dealServerLink(ServerPackage serverPackage) {
        synchronized (LinkMonitor.class) {
            // 服务端IP
            String ip = NetUtils.getLocalIp();
            String rootNodeTime = String.valueOf(System.currentTimeMillis());
            String[] linkArry = ArrayUtil.reverse(serverPackage.getChain().getNetworkChain().toArray(new String[]{}));
            String[] linkTimeArry = ArrayUtil.reverse(serverPackage.getChain().getTimeChain().toArray(new String[]{}));
            // 如果链路中的第一个IP与服务端IP相同，意味着是同一台服务器，需要去掉
            if (ArrayUtil.isNotEmpty(linkArry) && StringUtils.equals(linkArry[0], ip)) {
                linkArry = ArrayUtil.remove(linkArry, 0);
                linkTimeArry = ArrayUtil.remove(linkTimeArry, 0);
            }
            // 数组长度为0，意味着是同一台服务器，已经在上一步去除了
            if (ArrayUtil.isEmpty(linkArry)) {
                return;
            }
            String link = ArrayUtil.join(linkArry, ",");
            String linkTime = ArrayUtil.join(linkTimeArry, ",");
            // 先查询有没有此链路
            List<MonitorLink> monitorLinks = this.linkService.selectMonitorLinks(ip, link, MonitorTypeEnums.SERVER.name());
            // 有——>更新
            if (CollectionUtils.isNotEmpty(monitorLinks)) {
                MonitorLink monitorLink = monitorLinks.get(0);
                monitorLink.setRootNodeTime(rootNodeTime);
                monitorLink.setLinkTime(linkTime);
                monitorLink.setUpdateTime(new Date());
                this.linkService.updateById(monitorLink);
            }
            // 没有——>插入
            else {
                MonitorLink monitorLink = MonitorLink.builder()
                        .rootNode(ip)
                        .rootNodeTime(rootNodeTime)
                        .link(link)
                        .linkTime(linkTime)
                        .type(MonitorTypeEnums.SERVER.name())
                        .insertTime(new Date())
                        .build();
                this.linkService.save(monitorLink);
            }
        }
    }

}
