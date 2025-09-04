package com.gitee.pifeng.monitoring.ui.core;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.abs.AbstractPackageConstructor;
import com.gitee.pifeng.monitoring.common.abs.AbstractSuperPackage;
import com.gitee.pifeng.monitoring.common.constant.EndpointTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.LanguageTypeConstants;
import com.gitee.pifeng.monitoring.common.domain.Chain;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.AppServerDetectorUtils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.common.util.server.OsUtils;
import com.gitee.pifeng.monitoring.plug.core.ConfigLoader;
import com.gitee.pifeng.monitoring.plug.core.InstanceGenerator;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashSet;

/**
 * <p>
 * UI端包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/5 12:49
 */
@Component
public class UiPackageConstructor extends AbstractPackageConstructor {

    /**
     * <p>
     * 获取链路信息
     * </p>
     *
     * @param <T> 泛型的数据包类型
     * @param pkg 数据包
     * @return {@link Chain} 链路信息
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2023年5月21日 上午10:02:46
     */
    private <T extends AbstractSuperPackage> Chain getChain(T pkg) throws NetException {
        // 链路信息
        Chain chain = pkg.getChain();
        if (chain == null) {
            chain = Chain.builder().build();
        }
        // 应用链路
        LinkedHashSet<String> instanceChain = chain.getInstanceChain();
        // 网络链路
        LinkedHashSet<String> networkChain = chain.getNetworkChain();
        // 时间链路
        LinkedHashSet<String> timeChain = chain.getTimeChain();
        if (CollectionUtils.isEmpty(instanceChain)) {
            instanceChain = Sets.newLinkedHashSet();
        }
        if (CollectionUtils.isEmpty(networkChain)) {
            networkChain = Sets.newLinkedHashSet();
        }
        if (CollectionUtils.isEmpty(timeChain)) {
            timeChain = Sets.newLinkedHashSet();
        }
        String ip = ConfigLoader.getMonitoringProperties().getServerInfo().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.getMonitoringProperties().getServerInfo().getIp();
        networkChain.add(ip);
        chain.setNetworkChain(networkChain);
        instanceChain.add(InstanceGenerator.getInstanceId());
        chain.setInstanceChain(instanceChain);
        timeChain.add(String.valueOf(System.currentTimeMillis()));
        chain.setTimeChain(timeChain);
        return chain;
    }

    /**
     * <p>
     * 构造抽象父包数据
     * </p>
     *
     * @param <T> 泛型的数据包类型
     * @param pkg 数据包
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:02:46
     */
    private <T extends AbstractSuperPackage> void structureAbstractSuperPackage(T pkg) throws NetException {
        // 应用实例端点
        pkg.setInstanceEndpoint(EndpointTypeEnums.UI.getNameEn());
        // 应用实例ID
        pkg.setInstanceId(InstanceGenerator.getInstanceId());
        // 应用实例名
        pkg.setInstanceName(ConfigLoader.getMonitoringProperties().getInstance().getName());
        // 应用实例描述
        pkg.setInstanceDesc(ConfigLoader.getMonitoringProperties().getInstance().getDesc());
        // 应用实例程序语言
        pkg.setInstanceLanguage(LanguageTypeConstants.JAVA);
        // 应用服务器类型
        pkg.setAppServerType(AppServerDetectorUtils.getAppServerTypeEnum());
        // IP地址
        String ip = ConfigLoader.getMonitoringProperties().getServerInfo().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.getMonitoringProperties().getServerInfo().getIp();
        pkg.setIp(ip);
        // 计算机名
        pkg.setComputerName(OsUtils.getComputerName());
        // 链路信息
        pkg.setChain(this.getChain(pkg));
    }

    /**
     * <p>
     * 构造基础请求包数据
     * </p>
     *
     * @param <T>      泛型的数据包类型
     * @param pkg      数据包
     * @param extraMsg 附加信息
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:02:46
     */
    private <T extends BaseRequestPackage> void structureBaseRequestPackage(T pkg, JSONObject extraMsg) throws NetException {
        // 构造抽象父包数据
        this.structureAbstractSuperPackage(pkg);
        // 请求ID
        pkg.setId(IdUtil.randomUUID());
        // 请求时间
        pkg.setDateTime(new Date());
        // 附加信息
        pkg.setExtraMsg(extraMsg);
    }

    /**
     * <p>
     * 构建基础请求包
     * </p>
     *
     * @param extraMsg 附加信息
     * @return {@link BaseRequestPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2021/4/5 12:45
     */
    @Override
    public BaseRequestPackage structureBaseRequestPackage(JSONObject extraMsg) throws NetException {
        BaseRequestPackage baseRequestPackage = new BaseRequestPackage();
        // 构造基础请求包数据
        this.structureBaseRequestPackage(baseRequestPackage, extraMsg);
        return baseRequestPackage;
    }

}
