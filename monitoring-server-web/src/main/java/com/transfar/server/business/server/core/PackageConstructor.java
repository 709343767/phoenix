package com.transfar.server.business.server.core;

import com.google.common.base.Charsets;
import com.transfar.common.constant.EndpointTypeConstants;
import com.transfar.common.constant.ResultMsgConstants;
import com.transfar.common.domain.Alarm;
import com.transfar.common.domain.Result;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.common.dto.HeartbeatPackage;
import com.transfar.common.dto.ServerPackage;
import com.transfar.common.inf.IPackageConstructor;
import com.transfar.common.util.NetUtils;
import com.transfar.common.util.SigarUtils;
import com.transfar.common.util.StrUtils;
import com.transfar.server.util.InstanceUtils;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午3:31:11
 */
public class PackageConstructor implements IPackageConstructor {

    /**
     * <p>
     * 构建告警包
     * </p>
     *
     * @param alarm 告警
     * @return {@link AlarmPackage}
     * @author 皮锋
     * @custom.date 2020/3/13 11:14
     */
    @Override
    public AlarmPackage structureAlarmPackage(Alarm alarm) {
        AlarmPackage alarmPackage = new AlarmPackage();
        alarmPackage.setId(StrUtils.getUUID());
        alarmPackage.setDateTime(new Date());
        alarmPackage.setEndpoint(EndpointTypeConstants.SERVER);
        alarmPackage.setInstanceId(InstanceUtils.getInstanceId());
        alarmPackage.setInstanceName(InstanceUtils.getInstanceName());
        alarmPackage.setIp(NetUtils.getLocalIp());
        alarmPackage.setComputerName(SigarUtils.getComputerName());
        // 判断字符集
        Charset charset = alarm.getCharset();
        // 设置了字符集
        if (null != charset) {
            alarm.setMsg(new String(alarm.getMsg().getBytes(Charsets.UTF_8), Charsets.UTF_8));
            alarm.setCharset(Charsets.UTF_8);
        }
        alarmPackage.setAlarm(alarm);
        return alarmPackage;
    }

    @Override
    public HeartbeatPackage structureHeartbeatPackage() {
        return null;
    }

    @Override
    public ServerPackage structureServerPackage() {
        return null;
    }

    /**
     * <p>
     * 构建请求成功的基础响应包
     * </p>
     *
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月11日 上午9:52:37
     */
    @Override
    public BaseResponsePackage structureBaseResponsePackageBySuccess() {
        BaseResponsePackage baseResponsePackage = new BaseResponsePackage();
        baseResponsePackage.setEndpoint(EndpointTypeConstants.SERVER);
        baseResponsePackage.setInstanceId(InstanceUtils.getInstanceId());
        baseResponsePackage.setInstanceName(InstanceUtils.getInstanceName());
        baseResponsePackage.setIp(NetUtils.getLocalIp());
        baseResponsePackage.setComputerName(SigarUtils.getComputerName());
        baseResponsePackage.setId(StrUtils.getUUID());
        baseResponsePackage.setDateTime(new Date());
        baseResponsePackage.setResult(Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build());
        return baseResponsePackage;
    }

    /**
     * <p>
     * 构建请求失败的基础响应包
     * </p>
     *
     * @param msg 返回信息
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月11日 上午9:52:48
     */
    @Override
    public BaseResponsePackage structureBaseResponsePackageByFail(String msg) {
        BaseResponsePackage baseResponsePackage = new BaseResponsePackage();
        baseResponsePackage.setEndpoint(EndpointTypeConstants.SERVER);
        baseResponsePackage.setInstanceId(InstanceUtils.getInstanceId());
        baseResponsePackage.setInstanceName(InstanceUtils.getInstanceName());
        baseResponsePackage.setIp(NetUtils.getLocalIp());
        baseResponsePackage.setComputerName(SigarUtils.getComputerName());
        baseResponsePackage.setId(StrUtils.getUUID());
        baseResponsePackage.setDateTime(new Date());
        if (null == msg) {
            baseResponsePackage.setResult(Result.builder().isSuccess(false).msg(ResultMsgConstants.FAILURE).build());
        } else {
            baseResponsePackage.setResult(Result.builder().isSuccess(false).msg(msg).build());
        }
        return baseResponsePackage;
    }

}
