package com.transfar.business.server.core;

import java.util.Date;

import com.transfar.constant.EndpointTypeConstants;
import com.transfar.domain.Alarm;
import com.transfar.dto.AlarmPackage;
import com.transfar.dto.BaseResponsePackage;
import com.transfar.dto.HeartbeatPackage;
import com.transfar.dto.ServerPackage;
import com.transfar.inf.IPackageConstructor;
import com.transfar.util.InstanceUtils;
import com.transfar.util.StrUtils;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午3:31:11
 */
public class PackageConstructor implements IPackageConstructor {

    @Override
    public AlarmPackage structureAlarmPackage(Alarm alarm) {
        return null;
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
     * @return BaseResponsePackage
     * @author 皮锋
     * @custom.date 2020年3月11日 上午9:52:37
     */
    @Override
    public BaseResponsePackage structureBaseResponsePackageBySuccess() {
        BaseResponsePackage baseResponsePackage = new BaseResponsePackage();
        baseResponsePackage.setEndpoint(EndpointTypeConstants.SERVER);
        baseResponsePackage.setInstanceId(InstanceUtils.getInstanceId());
        baseResponsePackage.setInstanceName(InstanceUtils.getInstanceName());
        baseResponsePackage.setId(StrUtils.getUUID());
        baseResponsePackage.setDateTime(new Date());
        baseResponsePackage.setResult(true);
        return baseResponsePackage;

    }

    /**
     * <p>
     * 构建请求失败的基础响应包
     * </p>
     *
     * @return BaseResponsePackage
     * @author 皮锋
     * @custom.date 2020年3月11日 上午9:52:48
     */
    @Override
    public BaseResponsePackage structureBaseResponsePackageByFail() {
        BaseResponsePackage baseResponsePackage = new BaseResponsePackage();
        baseResponsePackage.setEndpoint(EndpointTypeConstants.SERVER);
        baseResponsePackage.setInstanceId(InstanceUtils.getInstanceId());
        baseResponsePackage.setInstanceName(InstanceUtils.getInstanceName());
        baseResponsePackage.setId(StrUtils.getUUID());
        baseResponsePackage.setDateTime(new Date());
        baseResponsePackage.setResult(false);
        return baseResponsePackage;
    }

}
