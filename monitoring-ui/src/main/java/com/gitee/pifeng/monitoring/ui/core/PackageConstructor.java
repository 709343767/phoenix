package com.gitee.pifeng.monitoring.ui.core;

import cn.hutool.core.util.IdUtil;
import com.gitee.pifeng.monitoring.common.abs.AbstractPackageConstructor;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.AppServerDetectorUtils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.common.util.server.OsUtils;
import com.gitee.pifeng.monitoring.plug.core.ConfigLoader;
import com.gitee.pifeng.monitoring.plug.util.InstanceUtils;
import org.hyperic.sigar.SigarException;

import java.util.Date;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/5 12:49
 */
public class PackageConstructor extends AbstractPackageConstructor {

    /**
     * <p>
     * 构建基础请求包
     * </p>
     *
     * @return {@link BaseRequestPackage}
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2021/4/5 12:45
     */
    @Override
    public BaseRequestPackage structureBaseRequestPackage() throws NetException, SigarException {
        BaseRequestPackage baseRequestPackage = new BaseRequestPackage();
        baseRequestPackage.setId(IdUtil.randomUUID());
        baseRequestPackage.setDateTime(new Date());
        baseRequestPackage.setInstanceEndpoint(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceEndpoint());
        baseRequestPackage.setInstanceId(InstanceUtils.getInstanceId());
        baseRequestPackage.setInstanceName(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceName());
        baseRequestPackage.setInstanceDesc(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceDesc());
        baseRequestPackage.setInstanceLanguage(ConfigLoader.MONITORING_PROPERTIES.getOwnProperties().getInstanceLanguage());
        baseRequestPackage.setAppServerType(AppServerDetectorUtils.getAppServerTypeEnum());
        baseRequestPackage.setIp(ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp() == null ? NetUtils.getLocalIp() : ConfigLoader.MONITORING_PROPERTIES.getServerInfoProperties().getIp());
        baseRequestPackage.setComputerName(OsUtils.getComputerName());
        return baseRequestPackage;
    }

}
