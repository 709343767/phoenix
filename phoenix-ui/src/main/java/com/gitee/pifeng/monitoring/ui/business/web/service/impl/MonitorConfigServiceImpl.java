package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.constant.EnterpriseEnums;
import com.gitee.pifeng.monitoring.common.constant.ProtocolTypeEnums;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.property.server.*;
import com.gitee.pifeng.monitoring.plug.core.Sender;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorConfigDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorConfig;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorConfigService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorConfigPageFormVo;
import com.gitee.pifeng.monitoring.ui.constant.UrlConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.PackageConstructor;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

/**
 * <p>
 * 监控配置服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-11-04
 */
@Service
public class MonitorConfigServiceImpl extends ServiceImpl<IMonitorConfigDao, MonitorConfig> implements IMonitorConfigService {

    /**
     * 监控配置数据访问对象
     */
    @Autowired
    private IMonitorConfigDao monitorConfigDao;

    /**
     * <p>
     * 获取监控配置页面表单信息
     * </p>
     *
     * @return 监控配置页面表单对象
     * @author 皮锋
     * @custom.date 2021/1/28 9:10
     */
    @Override
    public MonitorConfigPageFormVo getMonitorConfigPageFormInfo() {
        MonitorConfig monitorConfig = this.monitorConfigDao.selectOne(new LambdaQueryWrapper<>());
        Feature[] feature = new Feature[]{Feature.AllowComment, Feature.AllowUnQuotedFieldNames, Feature.AllowSingleQuotes};
        MonitoringProperties properties = JSON.parseObject(monitorConfig.getValue(), MonitoringProperties.class, feature);
        MonitorConfigPageFormVo monitorConfigPageFormVo = new MonitorConfigPageFormVo();
        monitorConfigPageFormVo.setThreshold(properties.getThreshold());
        monitorConfigPageFormVo.setNetEnable(properties.getNetworkProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setTcpIpEnable(properties.getTcpIpProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setAlarmEnable(properties.getAlarmProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setAlarmLevel(properties.getAlarmProperties().getLevelEnum().name());
        monitorConfigPageFormVo.setAlarmWay(AlarmWayEnums.enums2Strs(properties.getAlarmProperties().getWayEnums()));
        monitorConfigPageFormVo.setAlarmMailEmills(ArrayUtil.join(properties.getAlarmProperties().getMailProperties().getEmills(), ";"));
        monitorConfigPageFormVo.setAlarmSmsAddress(properties.getAlarmProperties().getSmsProperties().getAddress());
        monitorConfigPageFormVo.setAlarmSmsEnterprise(properties.getAlarmProperties().getSmsProperties().getEnterpriseEnum().name());
        monitorConfigPageFormVo.setAlarmSmsPhoneNumbers(ArrayUtil.join(properties.getAlarmProperties().getSmsProperties().getPhoneNumbers(), ";"));
        monitorConfigPageFormVo.setAlarmSmsProtocol(properties.getAlarmProperties().getSmsProperties().getProtocolTypeEnum().name());
        monitorConfigPageFormVo.setServerEnable(properties.getServerProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerCpuOverloadThreshold(properties.getServerProperties().getServerCpuProperties().getOverloadThreshold());
        monitorConfigPageFormVo.setServerCpuLevel(properties.getServerProperties().getServerCpuProperties().getLevelEnum().name());
        monitorConfigPageFormVo.setServerDiskOverloadThreshold(properties.getServerProperties().getServerDiskProperties().getOverloadThreshold());
        monitorConfigPageFormVo.setServerDiskLevel(properties.getServerProperties().getServerDiskProperties().getLevelEnum().name());
        monitorConfigPageFormVo.setServerMemoryOverloadThreshold(properties.getServerProperties().getServerMemoryProperties().getOverloadThreshold());
        monitorConfigPageFormVo.setServerMemoryLevel(properties.getServerProperties().getServerMemoryProperties().getLevelEnum().name());
        monitorConfigPageFormVo.setDbEnable(properties.getDbProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setDbTableSpaceOverloadThreshold(properties.getDbProperties().getDbTableSpaceProperties().getOverloadThreshold());
        monitorConfigPageFormVo.setDbTableSpaceLevel(properties.getDbProperties().getDbTableSpaceProperties().getLevelEnum().name());
        return monitorConfigPageFormVo;
    }

    /**
     * <p>
     * 更新监控配置
     * </p>
     *
     * @param monitorConfigPageFormVo 监控配置页面表单对象
     * @return layUiAdmin响应对象：如果更新数据库成功，LayUiAdminResultVo.data="success"；<br>
     * 如果更新数据库成功，但是更新监控服务端配置失败，LayUiAdminResultVo.data="refreshFail"；<br>
     * 否则，LayUiAdminResultVo.data="fail"。
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/11/9 20:11
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public LayUiAdminResultVo update(MonitorConfigPageFormVo monitorConfigPageFormVo) throws NetException, SigarException {
        // 告警邮箱配置属性
        MonitoringAlarmMailProperties monitoringAlarmMailProperties = new MonitoringAlarmMailProperties();
        monitoringAlarmMailProperties.setEmills(StrUtil.splitToArray(monitorConfigPageFormVo.getAlarmMailEmills(), ';'));
        // 告警短信配置属性
        MonitoringAlarmSmsProperties monitoringAlarmSmsProperties = new MonitoringAlarmSmsProperties();
        monitoringAlarmSmsProperties.setPhoneNumbers(StrUtil.splitToArray(monitorConfigPageFormVo.getAlarmSmsPhoneNumbers(), ';'));
        monitoringAlarmSmsProperties.setAddress(monitorConfigPageFormVo.getAlarmSmsAddress());
        monitoringAlarmSmsProperties.setProtocolTypeEnum(ProtocolTypeEnums.str2Enum(monitorConfigPageFormVo.getAlarmSmsProtocol()));
        monitoringAlarmSmsProperties.setEnterpriseEnum(EnterpriseEnums.str2Enum(monitorConfigPageFormVo.getAlarmSmsEnterprise()));
        // 告警配置属性
        MonitoringAlarmProperties monitoringAlarmProperties = new MonitoringAlarmProperties();
        monitoringAlarmProperties.setEnable(monitorConfigPageFormVo.getAlarmEnable() == 1);
        monitoringAlarmProperties.setLevelEnum(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getAlarmLevel()));
        monitoringAlarmProperties.setWayEnums(AlarmWayEnums.strs2Enums(monitorConfigPageFormVo.getAlarmWay()));
        monitoringAlarmProperties.setSmsProperties(monitoringAlarmSmsProperties);
        monitoringAlarmProperties.setMailProperties(monitoringAlarmMailProperties);
        // 网络配置属性
        MonitoringNetworkProperties monitoringNetworkProperties = new MonitoringNetworkProperties();
        monitoringNetworkProperties.setEnable(monitorConfigPageFormVo.getNetEnable() == 1);
        // TCP/IP配置属性
        MonitoringTcpIpProperties monitoringTcpIpProperties = new MonitoringTcpIpProperties();
        monitoringTcpIpProperties.setEnable(monitorConfigPageFormVo.getTcpIpEnable() == 1);
        // 服务器CPU配置属性
        MonitoringServerCpuProperties monitoringServerCpuProperties = new MonitoringServerCpuProperties();
        monitoringServerCpuProperties.setOverloadThreshold(monitorConfigPageFormVo.getServerCpuOverloadThreshold());
        monitoringServerCpuProperties.setLevelEnum(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getServerCpuLevel()));
        // 服务器磁盘配置属性
        MonitoringServerDiskProperties monitoringServerDiskProperties = new MonitoringServerDiskProperties();
        monitoringServerDiskProperties.setOverloadThreshold(monitorConfigPageFormVo.getServerDiskOverloadThreshold());
        monitoringServerDiskProperties.setLevelEnum(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getServerDiskLevel()));
        // 服务器内存配置属性
        MonitoringServerMemoryProperties monitoringServerMemoryProperties = new MonitoringServerMemoryProperties();
        monitoringServerMemoryProperties.setOverloadThreshold(monitorConfigPageFormVo.getServerMemoryOverloadThreshold());
        monitoringServerMemoryProperties.setLevelEnum(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getServerMemoryLevel()));
        // 服务器配置属性
        MonitoringServerProperties monitoringServerProperties = new MonitoringServerProperties();
        monitoringServerProperties.setEnable(monitorConfigPageFormVo.getServerEnable() == 1);
        monitoringServerProperties.setServerCpuProperties(monitoringServerCpuProperties);
        monitoringServerProperties.setServerDiskProperties(monitoringServerDiskProperties);
        monitoringServerProperties.setServerMemoryProperties(monitoringServerMemoryProperties);
        // 数据库表空间配置属性
        MonitoringDbTableSpaceProperties monitoringDbTableSpaceProperties = new MonitoringDbTableSpaceProperties();
        monitoringDbTableSpaceProperties.setOverloadThreshold(monitorConfigPageFormVo.getDbTableSpaceOverloadThreshold());
        monitoringDbTableSpaceProperties.setLevelEnum(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getDbTableSpaceLevel()));
        // 数据库配置
        MonitoringDbProperties monitoringDbProperties = new MonitoringDbProperties();
        monitoringDbProperties.setEnable(monitorConfigPageFormVo.getDbEnable() == 1);
        monitoringDbProperties.setDbTableSpaceProperties(monitoringDbTableSpaceProperties);
        // 监控配置属性
        MonitoringProperties properties = new MonitoringProperties();
        properties.setThreshold(monitorConfigPageFormVo.getThreshold());
        properties.setAlarmProperties(monitoringAlarmProperties);
        properties.setNetworkProperties(monitoringNetworkProperties);
        properties.setTcpIpProperties(monitoringTcpIpProperties);
        properties.setServerProperties(monitoringServerProperties);
        properties.setDbProperties(monitoringDbProperties);
        // 监控属性转json字符串
        String value = properties.toJsonString();
        // 查询数据库中是否有监控配置信息
        Integer selectCountDb = this.monitorConfigDao.selectCount(new LambdaQueryWrapper<>());
        // 没有
        if (selectCountDb == null || selectCountDb == 0) {
            this.monitorConfigDao.insert(MonitorConfig.builder().value(value).insertTime(new Date()).build());
        }
        // 有
        else {
            this.monitorConfigDao.update(MonitorConfig.builder().value(value).updateTime(new Date()).build(), new LambdaUpdateWrapper<>());
        }
        // 刷新服务端监控属性配置
        try {
            BaseRequestPackage baseRequestPackage = new PackageConstructor().structureBaseRequestPackage();
            String result = Sender.send(UrlConstants.MONITORING_PROPERTIES_CONFIG_REFRESH_URL, baseRequestPackage.toJsonString());
            BaseResponsePackage baseResponsePackage = JSON.parseObject(result, BaseResponsePackage.class);
            // 是否刷新配置成功
            boolean isSuccess = baseResponsePackage.getResult().isSuccess();
            if (isSuccess) {
                return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
            }
            return LayUiAdminResultVo.ok(WebResponseConstants.REFRESH_FAIL);
        } catch (IOException e) {
            log.error("刷新服务端配置异常！", e);
            return LayUiAdminResultVo.ok(WebResponseConstants.REFRESH_FAIL);
        }
    }

}
