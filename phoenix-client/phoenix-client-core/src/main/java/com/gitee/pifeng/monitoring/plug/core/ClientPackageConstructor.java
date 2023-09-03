package com.gitee.pifeng.monitoring.plug.core;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.abs.AbstractPackageConstructor;
import com.gitee.pifeng.monitoring.common.abs.AbstractSuperPackage;
import com.gitee.pifeng.monitoring.common.constant.LanguageTypeConstants;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Chain;
import com.gitee.pifeng.monitoring.common.domain.Jvm;
import com.gitee.pifeng.monitoring.common.domain.Server;
import com.gitee.pifeng.monitoring.common.dto.*;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.AppServerDetectorUtils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.common.util.server.OsUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * <p>
 * 客户端包构造器
 * </p>
 * <strong>饿汉式（静态常量）单例模式，来获取客户端包构造器，节省系统资源，提高系统性能。</strong>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午2:52:50
 */
public class ClientPackageConstructor extends AbstractPackageConstructor {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2023/5/31 8:27
     */
    private ClientPackageConstructor() {
    }

    /**
     * 客户端包构造器
     */
    private static final ClientPackageConstructor INSTANCE = new ClientPackageConstructor();

    /**
     * <p>
     * 单例模式获取客户端包构造器
     * </p>
     *
     * @return {@link ClientPackageConstructor}
     * @author 皮锋
     * @custom.date 2023/5/31 8:29
     */
    public static ClientPackageConstructor getInstance() {
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
        // 应用实例端点，从配置文件获取
        pkg.setInstanceEndpoint(ConfigLoader.getMonitoringProperties().getOwnProperties().getInstanceEndpoint());
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
        Date date = new Date();
        // 构造抽象父包数据
        this.structureAbstractSuperPackage(pkg);
        // 请求ID
        pkg.setId(IdUtil.randomUUID());
        // 请求时间
        pkg.setDateTime(date);
        // 附加信息
        pkg.setExtraMsg(extraMsg);
    }

    /**
     * <p>
     * 构造告警数据包
     * </p>
     *
     * @param alarm 告警信息
     * @return {@link AlarmPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:02:46
     */
    @Override
    public AlarmPackage structureAlarmPackage(Alarm alarm) throws NetException {
        AlarmPackage alarmPackage = new AlarmPackage();
        // 构造基础请求包数据
        this.structureBaseRequestPackage(alarmPackage, null);
        // 判断字符集
        Charset charset = alarm.getCharset();
        // 设置了字符集
        if (null != charset) {
            alarm.setTitle(new String(alarm.getTitle().getBytes(charset), StandardCharsets.UTF_8));
            alarm.setMsg(new String(alarm.getMsg().getBytes(charset), StandardCharsets.UTF_8));
            alarm.setCharset(StandardCharsets.UTF_8);
        }
        alarmPackage.setAlarm(alarm);
        return alarmPackage;
    }

    /**
     * <p>
     * 构建心跳数据包
     * </p>
     *
     * @return {@link HeartbeatPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:54:30
     */
    @Override
    public HeartbeatPackage structureHeartbeatPackage() throws NetException {
        HeartbeatPackage heartbeatPackage = new HeartbeatPackage();
        // 构造基础请求包数据
        this.structureBaseRequestPackage(heartbeatPackage, null);
        // 心跳频率
        heartbeatPackage.setRate(ConfigLoader.getMonitoringProperties().getHeartbeatProperties().getRate());
        return heartbeatPackage;
    }

    /**
     * <p>
     * 构建下线信息包
     * </p>
     *
     * @return {@link OfflinePackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2023/5/30 13:35
     */
    @Override
    public OfflinePackage structureOfflinePackage() throws NetException {
        OfflinePackage offlinePackage = new OfflinePackage();
        // 构造基础请求包数据
        this.structureBaseRequestPackage(offlinePackage, null);
        // 设置离线类型
        List<MonitorTypeEnums> monitorTypes = Lists.newArrayList();
        monitorTypes.add(MonitorTypeEnums.INSTANCE);
        offlinePackage.setMonitorTypes(monitorTypes);
        return offlinePackage;
    }

    /**
     * <p>
     * 构建服务器数据包
     * </p>
     *
     * @param server 服务器信息
     * @return {@link ServerPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午4:51:51
     */
    @Override
    public ServerPackage structureServerPackage(Server server) throws NetException {
        ServerPackage serverPackage = new ServerPackage();
        // 构造基础请求包数据
        this.structureBaseRequestPackage(serverPackage, null);
        // 设置服务器信息
        serverPackage.setServer(server);
        serverPackage.setRate(ConfigLoader.getMonitoringProperties().getServerInfoProperties().getRate());
        return serverPackage;
    }

    /**
     * <p>
     * 构建Java虚拟机信息包
     * </p>
     *
     * @param jvm Java虚拟机信息
     * @return {@link JvmPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/8/14 21:28
     */
    @Override
    public JvmPackage structureJvmPackage(Jvm jvm) throws NetException {
        JvmPackage jvmPackage = new JvmPackage();
        // 构造基础请求包数据
        this.structureBaseRequestPackage(jvmPackage, null);
        // 设置Java虚拟机信息
        jvmPackage.setJvm(jvm);
        jvmPackage.setRate(ConfigLoader.getMonitoringProperties().getJvmInfoProperties().getRate());
        return jvmPackage;
    }

}