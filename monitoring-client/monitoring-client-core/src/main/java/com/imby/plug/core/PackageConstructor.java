package com.imby.plug.core;

import com.google.common.base.Charsets;
import com.imby.common.constant.EndpointTypeConstants;
import com.imby.common.domain.Alarm;
import com.imby.common.domain.Result;
import com.imby.common.domain.server.ServerDomain;
import com.imby.common.dto.AlarmPackage;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.common.dto.ServerPackage;
import com.imby.common.inf.IPackageConstructor;
import com.imby.common.util.NetUtils;
import com.imby.common.util.SigarUtils;
import com.imby.common.util.StrUtils;
import com.imby.plug.util.InstanceUtils;
import org.hyperic.sigar.SigarException;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午2:52:50
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
        alarmPackage.setId(StrUtils.getUUID());
        alarmPackage.setDateTime(new Date());
        alarmPackage.setEndpoint(EndpointTypeConstants.CLIENT);
        alarmPackage.setInstanceId(InstanceUtils.getInstanceId());
        alarmPackage.setInstanceName(InstanceUtils.getInstanceName());
        alarmPackage.setInstanceDesc(InstanceUtils.getInstanceDesc());
        alarmPackage.setIp(NetUtils.getLocalIp());
        alarmPackage.setComputerName(SigarUtils.getComputerName());
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
        heartbeatPackage.setId(StrUtils.getUUID());
        heartbeatPackage.setEndpoint(EndpointTypeConstants.CLIENT);
        heartbeatPackage.setInstanceId(InstanceUtils.getInstanceId());
        heartbeatPackage.setInstanceName(InstanceUtils.getInstanceName());
        heartbeatPackage.setInstanceDesc(InstanceUtils.getInstanceDesc());
        heartbeatPackage.setIp(NetUtils.getLocalIp());
        heartbeatPackage.setComputerName(SigarUtils.getComputerName());
        heartbeatPackage.setDateTime(new Date());
        heartbeatPackage.setRate(ConfigLoader.monitoringProperties.getHeartbeatProperties().getRate());
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
        serverPackage.setId(StrUtils.getUUID());
        serverPackage.setDateTime(new Date());
        serverPackage.setEndpoint(EndpointTypeConstants.CLIENT);
        serverPackage.setInstanceId(InstanceUtils.getInstanceId());
        serverPackage.setInstanceName(InstanceUtils.getInstanceName());
        serverPackage.setInstanceDesc(InstanceUtils.getInstanceDesc());
        serverPackage.setIp(NetUtils.getLocalIp());
        serverPackage.setComputerName(SigarUtils.getComputerName());
        ServerDomain serverDomain = SigarUtils.getServerInfo();
        serverPackage.setServerDomain(serverDomain);
        serverPackage.setRate(ConfigLoader.monitoringProperties.getServerInfoProperties().getRate());
        return serverPackage;
    }

    @Override
    public BaseResponsePackage structureBaseResponsePackage(Result result) {
        return null;
    }

}
