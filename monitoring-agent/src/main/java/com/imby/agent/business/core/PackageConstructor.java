package com.imby.agent.business.core;

import cn.hutool.core.util.IdUtil;
import com.google.common.base.Charsets;
import com.imby.agent.util.InstanceUtils;
import com.imby.common.constant.EndpointTypeConstants;
import com.imby.common.domain.Alarm;
import com.imby.common.domain.Result;
import com.imby.common.dto.*;
import com.imby.common.inf.IPackageConstructor;
import com.imby.common.util.JvmUtils;
import com.imby.common.util.NetUtils;
import com.imby.common.util.OsUtils;
import com.imby.common.util.ServerUtils;
import org.hyperic.sigar.SigarException;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午12:16:59
 */
public class PackageConstructor implements IPackageConstructor {

    /**
     * <p>
     * 构造告警数据包
     * </p>
     *
     * @param alarm 告警信息
     * @return {@link AlarmPackage}
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:02:46
     */
    @Override
    public AlarmPackage structureAlarmPackage(Alarm alarm) {
        AlarmPackage alarmPackage = new AlarmPackage();
        alarmPackage.setId(IdUtil.randomUUID());
        alarmPackage.setDateTime(new Date());
        alarmPackage.setEndpoint(EndpointTypeConstants.AGENT);
        alarmPackage.setInstanceId(InstanceUtils.getInstanceId());
        alarmPackage.setInstanceName(InstanceUtils.getInstanceName());
        alarmPackage.setInstanceDesc(InstanceUtils.getInstanceDesc());
        alarmPackage.setIp(NetUtils.getLocalIp());
        alarmPackage.setComputerName(OsUtils.getComputerName());
        // 判断字符集
        Charset charset = alarm.getCharset();
        // 设置了字符集
        if (null != charset) {
            alarm.setTitle(new String(alarm.getTitle().getBytes(Charsets.UTF_8), Charsets.UTF_8));
            alarm.setMsg(new String(alarm.getMsg().getBytes(Charsets.UTF_8), Charsets.UTF_8));
            alarm.setCharset(Charsets.UTF_8);
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
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:54:30
     */
    @Override
    public HeartbeatPackage structureHeartbeatPackage() {
        HeartbeatPackage heartbeatPackage = new HeartbeatPackage();
        heartbeatPackage.setId(IdUtil.randomUUID());
        heartbeatPackage.setEndpoint(EndpointTypeConstants.AGENT);
        heartbeatPackage.setInstanceId(InstanceUtils.getInstanceId());
        heartbeatPackage.setInstanceName(InstanceUtils.getInstanceName());
        heartbeatPackage.setInstanceDesc(InstanceUtils.getInstanceDesc());
        heartbeatPackage.setIp(NetUtils.getLocalIp());
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
     * @return {@link ServerPackage}
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午4:51:51
     */
    @Override
    public ServerPackage structureServerPackage() throws SigarException {
        ServerPackage serverPackage = new ServerPackage();
        serverPackage.setId(IdUtil.randomUUID());
        serverPackage.setDateTime(new Date());
        serverPackage.setEndpoint(EndpointTypeConstants.AGENT);
        serverPackage.setInstanceId(InstanceUtils.getInstanceId());
        serverPackage.setInstanceName(InstanceUtils.getInstanceName());
        serverPackage.setInstanceDesc(InstanceUtils.getInstanceDesc());
        serverPackage.setIp(NetUtils.getLocalIp());
        serverPackage.setComputerName(OsUtils.getComputerName());
        serverPackage.setServer(ServerUtils.getServerInfo());
        serverPackage.setRate(ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getRate());
        return serverPackage;
    }

    /**
     * <p>
     * 构建Java虚拟机信息包
     * </p>
     *
     * @return {@link JvmPackage}
     * @author 皮锋
     * @custom.date 2020/8/14 21:28
     */
    @Override
    public JvmPackage structureJvmPackage() {
        JvmPackage jvmPackage = new JvmPackage();
        jvmPackage.setId(IdUtil.randomUUID());
        jvmPackage.setDateTime(new Date());
        jvmPackage.setEndpoint(EndpointTypeConstants.AGENT);
        jvmPackage.setInstanceId(InstanceUtils.getInstanceId());
        jvmPackage.setInstanceName(InstanceUtils.getInstanceName());
        jvmPackage.setInstanceDesc(InstanceUtils.getInstanceDesc());
        jvmPackage.setIp(NetUtils.getLocalIp());
        jvmPackage.setComputerName(OsUtils.getComputerName());
        jvmPackage.setJvm(JvmUtils.getJvmInfo());
        jvmPackage.setRate(ConfigLoader.MONITORING_PROPERTIES.getJvmInfoProperties().getRate());
        return jvmPackage;
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
    @Override
    public BaseResponsePackage structureBaseResponsePackage(Result result) {
        BaseResponsePackage baseResponsePackage = new BaseResponsePackage();
        baseResponsePackage.setEndpoint(EndpointTypeConstants.AGENT);
        baseResponsePackage.setInstanceId(InstanceUtils.getInstanceId());
        baseResponsePackage.setInstanceName(InstanceUtils.getInstanceName());
        baseResponsePackage.setInstanceDesc(InstanceUtils.getInstanceDesc());
        baseResponsePackage.setIp(NetUtils.getLocalIp());
        baseResponsePackage.setComputerName(OsUtils.getComputerName());
        baseResponsePackage.setDateTime(new Date());
        baseResponsePackage.setId(IdUtil.randomUUID());
        baseResponsePackage.setResult(result);
        return baseResponsePackage;
    }

}