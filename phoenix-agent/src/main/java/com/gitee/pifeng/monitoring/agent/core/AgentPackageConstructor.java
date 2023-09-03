package com.gitee.pifeng.monitoring.agent.core;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.abs.AbstractPackageConstructor;
import com.gitee.pifeng.monitoring.common.abs.AbstractSuperPackage;
import com.gitee.pifeng.monitoring.common.constant.EndpointTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.LanguageTypeConstants;
import com.gitee.pifeng.monitoring.common.domain.Chain;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.AppServerDetectorUtils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.common.util.server.OsUtils;
import com.gitee.pifeng.monitoring.plug.core.ConfigLoader;
import com.gitee.pifeng.monitoring.plug.core.InstanceGenerator;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashSet;

/**
 * <p>
 * 代理端包构造器
 * </p>
 * <strong>
 * 1.此类注入到了Spring容器，是Spring容器的一个Bean，可以通过Spring的方式获取该类的实例；<br>
 * 2.此类也实现了饿汉式（静态常量）单例模式，在非Spring容器管理的类中，也可通过 {@link AgentPackageConstructor#getInstance()}来获取单例实例；<br>
 * 3.为了节省系统资源，提高系统性能，不要使用new来创建此类的实例。
 * </strong>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午12:16:59
 */
@Component
public class AgentPackageConstructor extends AbstractPackageConstructor {

    /**
     * 代理端包构造器
     */
    private static final AgentPackageConstructor INSTANCE = new AgentPackageConstructor();

    /**
     * <p>
     * 单例模式获取代理端包构造器
     * </p>
     *
     * @return {@link AgentPackageConstructor}
     * @author 皮锋
     * @custom.date 2023/5/31 8:29
     */
    public static AgentPackageConstructor getInstance() {
        return INSTANCE;
    }

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
    public <T extends AbstractSuperPackage> Chain getChain(T pkg) throws NetException {
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
        String ip = ConfigLoader.getMonitoringProperties().getServerInfoProperties().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.getMonitoringProperties().getServerInfoProperties().getIp();
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
        pkg.setInstanceEndpoint(EndpointTypeEnums.AGENT.getNameEn());
        // 应用实例ID
        pkg.setInstanceId(InstanceGenerator.getInstanceId());
        // 应用实例名
        pkg.setInstanceName(ConfigLoader.getMonitoringProperties().getOwnProperties().getInstanceName());
        // 应用实例描述
        pkg.setInstanceDesc(ConfigLoader.getMonitoringProperties().getOwnProperties().getInstanceDesc());
        // 应用实例程序语言
        pkg.setInstanceLanguage(LanguageTypeConstants.JAVA);
        // 应用服务器类型
        pkg.setAppServerType(AppServerDetectorUtils.getAppServerTypeEnum());
        // IP地址
        String ip = ConfigLoader.getMonitoringProperties().getServerInfoProperties().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.getMonitoringProperties().getServerInfoProperties().getIp();
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
     * 构造基础响应包数据
     * </p>
     *
     * @param <T>    泛型的数据包类型
     * @param pkg    数据包
     * @param result 返回结果
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:02:46
     */
    private <T extends BaseResponsePackage> void structureBaseResponsePackage(T pkg, Result result) throws NetException {
        // 构造抽象父包数据
        this.structureAbstractSuperPackage(pkg);
        // 响应ID
        pkg.setId(IdUtil.randomUUID());
        // 响应时间
        pkg.setDateTime(new Date());
        // 响应结果
        pkg.setResult(result);
    }

    /**
     * <p>
     * 构建请求失败的基础响应包
     * </p>
     *
     * @param result 返回结果
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月11日 上午9:52:48
     */
    @SneakyThrows
    @Override
    public BaseResponsePackage structureBaseResponsePackage(Result result) {
        BaseResponsePackage baseResponsePackage = new BaseResponsePackage();
        // 构造基础响应包数据
        this.structureBaseResponsePackage(baseResponsePackage, result);
        return baseResponsePackage;
    }

}