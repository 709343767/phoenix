package com.imby.agent.core;

import cn.hutool.core.util.IdUtil;
import com.imby.common.abs.AbstractPackageConstructor;
import com.imby.common.constant.EndpointTypeEnums;
import com.imby.common.constant.LanguageTypeConstants;
import com.imby.common.domain.Result;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.util.AppServerDetectorUtils;
import com.imby.common.util.NetUtils;
import com.imby.common.util.OsUtils;
import com.imby.plug.core.ConfigLoader;
import com.imby.plug.util.InstanceUtils;
import lombok.SneakyThrows;

import java.util.Date;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午12:16:59
 */
public class PackageConstructor extends AbstractPackageConstructor {

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
        baseResponsePackage.setInstanceEndpoint(EndpointTypeEnums.AGENT.getNameEn());
        baseResponsePackage.setInstanceId(InstanceUtils.getInstanceId());
        baseResponsePackage.setInstanceName(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceName());
        baseResponsePackage.setInstanceDesc(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceDesc());
        baseResponsePackage.setInstanceLanguage(LanguageTypeConstants.JAVA);
        baseResponsePackage.setAppServerType(AppServerDetectorUtils.getAppServerTypeEnum());
        baseResponsePackage.setIp(NetUtils.getLocalIp());
        baseResponsePackage.setComputerName(OsUtils.getComputerName());
        baseResponsePackage.setDateTime(new Date());
        baseResponsePackage.setId(IdUtil.randomUUID());
        baseResponsePackage.setResult(result);
        return baseResponsePackage;
    }

}