package com.gitee.pifeng.server.business.server.core;

import cn.hutool.core.util.IdUtil;
import com.gitee.pifeng.common.abs.AbstractPackageConstructor;
import com.gitee.pifeng.common.constant.EndpointTypeEnums;
import com.gitee.pifeng.common.constant.LanguageTypeConstants;
import com.gitee.pifeng.common.domain.Alarm;
import com.gitee.pifeng.common.domain.Result;
import com.gitee.pifeng.common.dto.AlarmPackage;
import com.gitee.pifeng.common.dto.BaseResponsePackage;
import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.common.util.AppServerDetectorUtils;
import com.gitee.pifeng.common.util.NetUtils;
import com.gitee.pifeng.common.util.OsUtils;
import com.gitee.pifeng.server.util.InstanceUtils;
import lombok.SneakyThrows;
import org.hyperic.sigar.SigarException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午3:31:11
 */
public class PackageConstructor extends AbstractPackageConstructor {

    /**
     * <p>
     * 构建告警包
     * </p>
     *
     * @param alarm 告警
     * @return {@link AlarmPackage}
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/13 11:14
     */
    @Override
    public AlarmPackage structureAlarmPackage(Alarm alarm) throws NetException, SigarException {
        AlarmPackage alarmPackage = new AlarmPackage();
        alarmPackage.setId(IdUtil.randomUUID());
        alarmPackage.setDateTime(new Date());
        alarmPackage.setInstanceEndpoint(EndpointTypeEnums.SERVER.getNameEn());
        alarmPackage.setInstanceId(InstanceUtils.getInstanceId());
        alarmPackage.setInstanceName(InstanceUtils.getInstanceName());
        alarmPackage.setInstanceDesc(InstanceUtils.getInstanceDesc());
        alarmPackage.setInstanceLanguage(LanguageTypeConstants.JAVA);
        alarmPackage.setAppServerType(AppServerDetectorUtils.getAppServerTypeEnum());
        alarmPackage.setIp(NetUtils.getLocalIp());
        alarmPackage.setComputerName(OsUtils.getComputerName());
        // 判断字符集
        Charset charset = alarm.getCharset();
        // 设置了字符集
        if (null != charset) {
            alarm.setTitle(new String(alarm.getTitle().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            alarm.setMsg(new String(alarm.getMsg().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            alarm.setCharset(StandardCharsets.UTF_8);
        }
        alarmPackage.setAlarm(alarm);
        return alarmPackage;
    }

    /**
     * <p>
     * 构建请求基础响应包
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
        baseResponsePackage.setInstanceEndpoint(EndpointTypeEnums.SERVER.getNameEn());
        baseResponsePackage.setInstanceId(InstanceUtils.getInstanceId());
        baseResponsePackage.setInstanceName(InstanceUtils.getInstanceName());
        baseResponsePackage.setInstanceDesc(InstanceUtils.getInstanceDesc());
        baseResponsePackage.setInstanceLanguage(LanguageTypeConstants.JAVA);
        baseResponsePackage.setAppServerType(AppServerDetectorUtils.getAppServerTypeEnum());
        baseResponsePackage.setIp(NetUtils.getLocalIp());
        baseResponsePackage.setComputerName(OsUtils.getComputerName());
        baseResponsePackage.setId(IdUtil.randomUUID());
        baseResponsePackage.setDateTime(new Date());
        baseResponsePackage.setResult(result);
        return baseResponsePackage;
    }

}