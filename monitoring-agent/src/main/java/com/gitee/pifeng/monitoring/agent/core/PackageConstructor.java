package com.gitee.pifeng.monitoring.agent.core;

import cn.hutool.core.util.IdUtil;
import com.gitee.pifeng.monitoring.common.abs.AbstractPackageConstructor;
import com.gitee.pifeng.monitoring.common.constant.EndpointTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.LanguageTypeConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.util.AppServerDetectorUtils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.common.util.server.OsUtils;
import com.gitee.pifeng.monitoring.plug.core.ConfigLoader;
import com.gitee.pifeng.monitoring.plug.util.InstanceUtils;
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
        baseResponsePackage.setIp(ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp());
        baseResponsePackage.setComputerName(OsUtils.getComputerName());
        baseResponsePackage.setDateTime(new Date());
        baseResponsePackage.setId(IdUtil.randomUUID());
        baseResponsePackage.setResult(result);
        return baseResponsePackage;
    }

}