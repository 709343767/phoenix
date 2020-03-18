package com.transfar.plug.core;

import com.google.common.base.Charsets;
import com.transfar.common.constant.EndpointTypeConstants;
import com.transfar.common.domain.Alarm;
import com.transfar.common.domain.server.ServerDomain;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.common.dto.HeartbeatPackage;
import com.transfar.common.dto.ServerPackage;
import com.transfar.common.inf.IPackageConstructor;
import com.transfar.common.util.NetUtils;
import com.transfar.common.util.SigarUtils;
import com.transfar.common.util.StrUtils;
import com.transfar.plug.util.InstanceUtils;
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
     * @return AlarmPackage
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:02:46
     */
    @Override
    public AlarmPackage structureAlarmPackage(Alarm alarm) {
        AlarmPackage alarmPackage = new AlarmPackage();
        alarmPackage.setId(StrUtils.getUUID());
        alarmPackage.setAlarmTime(new Date());
        alarmPackage.setEndpoint(EndpointTypeConstants.CLIENT);
        alarmPackage.setInstanceId(InstanceUtils.getInstanceId());
        alarmPackage.setInstanceName(InstanceUtils.getInstanceName());
        alarmPackage.setIp(NetUtils.getLocalHostAddress());
        alarmPackage.setLevel(alarm.getAlarmLevel().name());
        alarmPackage.setTest(alarm.isTest());
        alarmPackage.setTitle(alarm.getTitle());
        Charset charset = alarm.getCharset();
        // 设置了字符集
        if (null != charset) {
            alarmPackage.setMsg(new String(alarm.getMsg().getBytes(Charsets.UTF_8), Charsets.UTF_8));
        } else {
            alarmPackage.setMsg(alarm.getMsg());
        }
        return alarmPackage;
    }

    /**
     * <p>
     * 构建心跳数据包
     * </p>
     *
     * @return HeartbeatPackage
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
        heartbeatPackage.setIp(NetUtils.getLocalHostAddress());
        heartbeatPackage.setDateTime(new Date());
        heartbeatPackage.setRate(ConfigLoader.monitoringProperties.getHeartbeatProperties().getRate());
        return heartbeatPackage;
    }

    /**
     * <p>
     * 构建服务器数据包
     * </p>
     *
     * @return ServerPackage
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
        serverPackage.setIp(NetUtils.getLocalHostAddress());
        ServerDomain serverDomain = SigarUtils.getServerInfo();
        serverPackage.setServerDomain(serverDomain);
        return serverPackage;
    }

    @Override
    public BaseResponsePackage structureBaseResponsePackageBySuccess() {
        return null;
    }

    @Override
    public BaseResponsePackage structureBaseResponsePackageByFail() {
        return null;
    }

}
