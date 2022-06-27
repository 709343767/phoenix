package com.gitee.pifeng.monitoring.plug.core;

import cn.hutool.core.util.IdUtil;
import com.gitee.pifeng.monitoring.common.abs.AbstractPackageConstructor;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Jvm;
import com.gitee.pifeng.monitoring.common.domain.Server;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.dto.HeartbeatPackage;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.AppServerDetectorUtils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.common.util.server.OsUtils;
import com.gitee.pifeng.monitoring.plug.util.InstanceUtils;
import org.hyperic.sigar.SigarException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.TreeSet;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午2:52:50
 */
public class PackageConstructor extends AbstractPackageConstructor {

    /**
     * <p>
     * 构造告警数据包
     * </p>
     *
     * @param alarm 告警信息
     * @return {@link AlarmPackage}
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:02:46
     */
    @Override
    public AlarmPackage structureAlarmPackage(Alarm alarm) throws NetException, SigarException {
        AlarmPackage alarmPackage = new AlarmPackage();
        alarmPackage.setId(IdUtil.randomUUID());
        alarmPackage.setDateTime(new Date());
        alarmPackage.setInstanceEndpoint(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceEndpoint());
        alarmPackage.setInstanceId(InstanceUtils.getInstanceId());
        alarmPackage.setInstanceName(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceName());
        alarmPackage.setInstanceDesc(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceDesc());
        alarmPackage.setInstanceLanguage(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceLanguage());
        alarmPackage.setAppServerType(AppServerDetectorUtils.getAppServerTypeEnum());
        String ip = ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp();
        alarmPackage.setIp(ip);
        TreeSet<String> networkChain = new TreeSet<>();
        networkChain.add(ip);
        alarmPackage.setNetworkChain(networkChain);
        alarmPackage.setComputerName(OsUtils.getComputerName());
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
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:54:30
     */
    @Override
    public HeartbeatPackage structureHeartbeatPackage() throws NetException, SigarException {
        HeartbeatPackage heartbeatPackage = new HeartbeatPackage();
        heartbeatPackage.setId(IdUtil.randomUUID());
        heartbeatPackage.setInstanceEndpoint(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceEndpoint());
        heartbeatPackage.setInstanceId(InstanceUtils.getInstanceId());
        heartbeatPackage.setInstanceName(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceName());
        heartbeatPackage.setInstanceDesc(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceDesc());
        heartbeatPackage.setInstanceLanguage(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceLanguage());
        heartbeatPackage.setAppServerType(AppServerDetectorUtils.getAppServerTypeEnum());
        String ip = ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp();
        heartbeatPackage.setIp(ip);
        TreeSet<String> networkChain = new TreeSet<>();
        networkChain.add(ip);
        heartbeatPackage.setNetworkChain(networkChain);
        heartbeatPackage.setComputerName(OsUtils.getComputerName());
        heartbeatPackage.setDateTime(new Date());
        heartbeatPackage.setRate(ConfigLoader.MONITORING_PROPERTIES.getHeartbeatProperties().getRate());
        return heartbeatPackage;
    }

    /**
     * <p>
     * 构建服务器数据包
     * </p>
     *
     * @param server 服务器信息
     * @return {@link ServerPackage}
     * @throws SigarException Sigar异常
     * @throws NetException   获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午4:51:51
     */
    @Override
    public ServerPackage structureServerPackage(Server server) throws SigarException, NetException {
        ServerPackage serverPackage = new ServerPackage();
        serverPackage.setId(IdUtil.randomUUID());
        serverPackage.setDateTime(new Date());
        serverPackage.setInstanceEndpoint(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceEndpoint());
        serverPackage.setInstanceId(InstanceUtils.getInstanceId());
        serverPackage.setInstanceName(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceName());
        serverPackage.setInstanceDesc(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceDesc());
        serverPackage.setInstanceLanguage(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceLanguage());
        serverPackage.setAppServerType(AppServerDetectorUtils.getAppServerTypeEnum());
        String ip = ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp();
        serverPackage.setIp(ip);
        TreeSet<String> networkChain = new TreeSet<>();
        networkChain.add(ip);
        serverPackage.setNetworkChain(networkChain);
        serverPackage.setComputerName(OsUtils.getComputerName());
        // 设置服务器信息
        serverPackage.setServer(server);
        serverPackage.setRate(ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getRate());
        return serverPackage;
    }

    /**
     * <p>
     * 构建Java虚拟机信息包
     * </p>
     *
     * @param jvm Java虚拟机信息
     * @return {@link JvmPackage}
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/8/14 21:28
     */
    @Override
    public JvmPackage structureJvmPackage(Jvm jvm) throws NetException, SigarException {
        JvmPackage jvmPackage = new JvmPackage();
        jvmPackage.setId(IdUtil.randomUUID());
        jvmPackage.setDateTime(new Date());
        jvmPackage.setInstanceEndpoint(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceEndpoint());
        jvmPackage.setInstanceId(InstanceUtils.getInstanceId());
        jvmPackage.setInstanceName(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceName());
        jvmPackage.setInstanceDesc(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceDesc());
        jvmPackage.setInstanceLanguage(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceLanguage());
        jvmPackage.setAppServerType(AppServerDetectorUtils.getAppServerTypeEnum());
        String ip = ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp();
        jvmPackage.setIp(ip);
        TreeSet<String> networkChain = new TreeSet<>();
        networkChain.add(ip);
        jvmPackage.setNetworkChain(networkChain);
        jvmPackage.setComputerName(OsUtils.getComputerName());
        // 设置Java虚拟机信息
        jvmPackage.setJvm(jvm);
        jvmPackage.setRate(ConfigLoader.MONITORING_PROPERTIES.getJvmInfoProperties().getRate());
        return jvmPackage;
    }

}
