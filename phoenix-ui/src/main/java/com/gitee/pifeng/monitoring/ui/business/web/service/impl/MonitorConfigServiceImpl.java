package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.CommProtocolTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.EnterpriseEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
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
import com.gitee.pifeng.monitoring.ui.core.UiPackageConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
     * UI端包构造器
     */
    @Autowired
    private UiPackageConstructor uiPackageConstructor;

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
        MonitorConfig monitorConfig = this.baseMapper.selectOne(new LambdaQueryWrapper<>());
        Feature[] feature = new Feature[]{Feature.AllowComment, Feature.AllowUnQuotedFieldNames, Feature.AllowSingleQuotes};
        MonitoringProperties properties = JSON.parseObject(monitorConfig.getValue(), MonitoringProperties.class, feature);
        MonitorConfigPageFormVo monitorConfigPageFormVo = new MonitorConfigPageFormVo();
        monitorConfigPageFormVo.setThreshold(properties.getThreshold());
        monitorConfigPageFormVo.setNetEnable(properties.getNetworkProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setNetStatusEnable(properties.getNetworkProperties().getNetworkStatusProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setNetStatusAlarmEnable(properties.getNetworkProperties().getNetworkStatusProperties().isAlarmEnable() ? 1 : 0);
        monitorConfigPageFormVo.setTcpEnable(properties.getTcpProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setTcpStatusEnable(properties.getTcpProperties().getTcpStatusProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setTcpStatusAlarmEnable(properties.getTcpProperties().getTcpStatusProperties().isAlarmEnable() ? 1 : 0);
        monitorConfigPageFormVo.setHttpEnable(properties.getHttpProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setHttpStatusEnable(properties.getHttpProperties().getHttpStatusProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setHttpStatusAlarmEnable(properties.getHttpProperties().getHttpStatusProperties().isAlarmEnable() ? 1 : 0);
        monitorConfigPageFormVo.setAlarmEnable(properties.getAlarmProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setAlarmSilenceEnable(properties.getAlarmProperties().isSilenceEnable() ? 1 : 0);
        monitorConfigPageFormVo.setAlarmSilenceTimeSlot(properties.getAlarmProperties().getSilenceStartTime().format(DateTimeFormatter.ISO_LOCAL_TIME) + " - " + properties.getAlarmProperties().getSilenceEndTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
        monitorConfigPageFormVo.setAlarmLevel(properties.getAlarmProperties().getLevelEnum().name());
        monitorConfigPageFormVo.setAlarmWay(AlarmWayEnums.enums2Strs(properties.getAlarmProperties().getWayEnums()));
        monitorConfigPageFormVo.setAlarmMailboxEmails(ArrayUtil.join(properties.getAlarmProperties().getMailProperties().getEmails(), ";"));
        monitorConfigPageFormVo.setAlarmSmsAddress(properties.getAlarmProperties().getSmsProperties().getAddress());
        monitorConfigPageFormVo.setAlarmSmsEnterprise(properties.getAlarmProperties().getSmsProperties().getEnterpriseEnum().name());
        monitorConfigPageFormVo.setAlarmSmsPhoneNumbers(ArrayUtil.join(properties.getAlarmProperties().getSmsProperties().getPhoneNumbers(), ";"));
        monitorConfigPageFormVo.setAlarmSmsProtocol(properties.getAlarmProperties().getSmsProperties().getProtocolTypeEnum().name());
        monitorConfigPageFormVo.setServerEnable(properties.getServerProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerStatusEnable(properties.getServerProperties().getServerStatusProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerStatusAlarmEnable(properties.getServerProperties().getServerStatusProperties().isAlarmEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerCpuEnable(properties.getServerProperties().getServerCpuProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerCpuAlarmEnable(properties.getServerProperties().getServerCpuProperties().isAlarmEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerCpuOverloadThreshold(properties.getServerProperties().getServerCpuProperties().getOverloadThreshold());
        monitorConfigPageFormVo.setServerCpuLevel(properties.getServerProperties().getServerCpuProperties().getLevelEnum().name());
        monitorConfigPageFormVo.setServerLoadAverageEnable(properties.getServerProperties().getServerLoadAverageProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerLoadAverageAlarmEnable(properties.getServerProperties().getServerLoadAverageProperties().isAlarmEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerOverloadThreshold15minutes(properties.getServerProperties().getServerLoadAverageProperties().getOverloadThreshold15minutes());
        monitorConfigPageFormVo.setServerOverloadLevel15minutes(properties.getServerProperties().getServerLoadAverageProperties().getLevelEnum15minutes().name());
        monitorConfigPageFormVo.setServerDiskEnable(properties.getServerProperties().getServerDiskProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerDiskAlarmEnable(properties.getServerProperties().getServerDiskProperties().isAlarmEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerDiskOverloadThreshold(properties.getServerProperties().getServerDiskProperties().getOverloadThreshold());
        monitorConfigPageFormVo.setServerDiskLevel(properties.getServerProperties().getServerDiskProperties().getLevelEnum().name());
        monitorConfigPageFormVo.setServerMemoryEnable(properties.getServerProperties().getServerMemoryProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerMemoryAlarmEnable(properties.getServerProperties().getServerMemoryProperties().isAlarmEnable() ? 1 : 0);
        monitorConfigPageFormVo.setServerMemoryOverloadThreshold(properties.getServerProperties().getServerMemoryProperties().getOverloadThreshold());
        monitorConfigPageFormVo.setServerMemoryLevel(properties.getServerProperties().getServerMemoryProperties().getLevelEnum().name());
        monitorConfigPageFormVo.setDbEnable(properties.getDbProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setDbStatusEnable(properties.getDbProperties().getDbStatusProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setDbStatusAlarmEnable(properties.getDbProperties().getDbStatusProperties().isAlarmEnable() ? 1 : 0);
        monitorConfigPageFormVo.setDbTableSpaceEnable(properties.getDbProperties().getDbTableSpaceProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setDbTableSpaceAlarmEnable(properties.getDbProperties().getDbTableSpaceProperties().isAlarmEnable() ? 1 : 0);
        monitorConfigPageFormVo.setDbTableSpaceOverloadThreshold(properties.getDbProperties().getDbTableSpaceProperties().getOverloadThreshold());
        monitorConfigPageFormVo.setDbTableSpaceLevel(properties.getDbProperties().getDbTableSpaceProperties().getLevelEnum().name());
        monitorConfigPageFormVo.setInstanceEnable(properties.getInstanceProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setInstanceStatusEnable(properties.getInstanceProperties().getInstanceStatusProperties().isEnable() ? 1 : 0);
        monitorConfigPageFormVo.setInstanceStatusAlarmEnable(properties.getInstanceProperties().getInstanceStatusProperties().isAlarmEnable() ? 1 : 0);
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
    public LayUiAdminResultVo update(MonitorConfigPageFormVo monitorConfigPageFormVo) throws NetException, SigarException {
        // 告警邮箱配置属性
        MonitoringAlarmMailProperties monitoringAlarmMailProperties = new MonitoringAlarmMailProperties();
        monitoringAlarmMailProperties.setEmails(StrUtil.splitToArray(monitorConfigPageFormVo.getAlarmMailboxEmails(), ';'));
        // 告警短信配置属性
        MonitoringAlarmSmsProperties monitoringAlarmSmsProperties = new MonitoringAlarmSmsProperties();
        monitoringAlarmSmsProperties.setPhoneNumbers(StrUtil.splitToArray(monitorConfigPageFormVo.getAlarmSmsPhoneNumbers(), ';'));
        monitoringAlarmSmsProperties.setAddress(monitorConfigPageFormVo.getAlarmSmsAddress());
        monitoringAlarmSmsProperties.setProtocolTypeEnum(CommProtocolTypeEnums.str2Enum(monitorConfigPageFormVo.getAlarmSmsProtocol()));
        monitoringAlarmSmsProperties.setEnterpriseEnum(EnterpriseEnums.str2Enum(monitorConfigPageFormVo.getAlarmSmsEnterprise()));
        // 告警配置属性
        MonitoringAlarmProperties monitoringAlarmProperties = new MonitoringAlarmProperties();
        monitoringAlarmProperties.setEnable(monitorConfigPageFormVo.getAlarmEnable() == 1);
        monitoringAlarmProperties.setSilenceEnable(monitorConfigPageFormVo.getAlarmSilenceEnable() == 1);
        String[] alarmSilenceTimeSlot = monitorConfigPageFormVo.getAlarmSilenceTimeSlot().split(" - ");
        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        monitoringAlarmProperties.setSilenceStartTime(LocalTime.parse(StringUtils.trim(alarmSilenceTimeSlot[0]), formatter));
        monitoringAlarmProperties.setSilenceEndTime(LocalTime.parse(StringUtils.trim(alarmSilenceTimeSlot[1]), formatter));
        monitoringAlarmProperties.setLevelEnum(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getAlarmLevel()));
        monitoringAlarmProperties.setWayEnums(AlarmWayEnums.strs2Enums(monitorConfigPageFormVo.getAlarmWay()));
        monitoringAlarmProperties.setSmsProperties(monitoringAlarmSmsProperties);
        monitoringAlarmProperties.setMailProperties(monitoringAlarmMailProperties);
        // 网络状态配置属性
        MonitoringNetworkStatusProperties monitoringNetworkStatusProperties = new MonitoringNetworkStatusProperties();
        monitoringNetworkStatusProperties.setEnable(monitorConfigPageFormVo.getNetStatusEnable() == 1);
        monitoringNetworkStatusProperties.setAlarmEnable(monitorConfigPageFormVo.getNetStatusAlarmEnable() == 1);
        // 网络配置属性
        MonitoringNetworkProperties monitoringNetworkProperties = new MonitoringNetworkProperties();
        monitoringNetworkProperties.setEnable(monitorConfigPageFormVo.getNetEnable() == 1);
        monitoringNetworkProperties.setNetworkStatusProperties(monitoringNetworkStatusProperties);
        // TCP状态配置属性
        MonitoringTcpStatusProperties monitoringTcpStatusProperties = new MonitoringTcpStatusProperties();
        monitoringTcpStatusProperties.setEnable(monitorConfigPageFormVo.getTcpStatusEnable() == 1);
        monitoringTcpStatusProperties.setAlarmEnable(monitorConfigPageFormVo.getTcpStatusAlarmEnable() == 1);
        // TCP配置属性
        MonitoringTcpProperties monitoringTcpProperties = new MonitoringTcpProperties();
        monitoringTcpProperties.setEnable(monitorConfigPageFormVo.getTcpEnable() == 1);
        monitoringTcpProperties.setTcpStatusProperties(monitoringTcpStatusProperties);
        // HTTP状态配置属性
        MonitoringHttpStatusProperties monitoringHttpStatusProperties = new MonitoringHttpStatusProperties();
        monitoringHttpStatusProperties.setEnable(monitorConfigPageFormVo.getHttpStatusEnable() == 1);
        monitoringHttpStatusProperties.setAlarmEnable(monitorConfigPageFormVo.getHttpStatusAlarmEnable() == 1);
        // HTTP配置属性
        MonitoringHttpProperties monitoringHttpProperties = new MonitoringHttpProperties();
        monitoringHttpProperties.setEnable(monitorConfigPageFormVo.getHttpEnable() == 1);
        monitoringHttpProperties.setHttpStatusProperties(monitoringHttpStatusProperties);
        // 应用实例状态配置属性
        MonitoringInstanceStatusProperties monitoringInstanceStatusProperties = new MonitoringInstanceStatusProperties();
        monitoringInstanceStatusProperties.setEnable(monitorConfigPageFormVo.getInstanceStatusEnable() == 1);
        monitoringInstanceStatusProperties.setAlarmEnable(monitorConfigPageFormVo.getInstanceStatusAlarmEnable() == 1);
        // 应用实例配置属性
        MonitoringInstanceProperties monitoringInstanceProperties = new MonitoringInstanceProperties();
        monitoringInstanceProperties.setEnable(monitorConfigPageFormVo.getInstanceEnable() == 1);
        monitoringInstanceProperties.setInstanceStatusProperties(monitoringInstanceStatusProperties);
        // 服务器状态配置属性
        MonitoringServerStatusProperties monitoringServerStatusProperties = new MonitoringServerStatusProperties();
        monitoringServerStatusProperties.setEnable(monitorConfigPageFormVo.getServerStatusEnable() == 1);
        monitoringServerStatusProperties.setAlarmEnable(monitorConfigPageFormVo.getServerStatusAlarmEnable() == 1);
        // 服务器CPU配置属性
        MonitoringServerCpuProperties monitoringServerCpuProperties = new MonitoringServerCpuProperties();
        monitoringServerCpuProperties.setEnable(monitorConfigPageFormVo.getServerCpuEnable() == 1);
        monitoringServerCpuProperties.setAlarmEnable(monitorConfigPageFormVo.getServerCpuAlarmEnable() == 1);
        monitoringServerCpuProperties.setOverloadThreshold(monitorConfigPageFormVo.getServerCpuOverloadThreshold());
        monitoringServerCpuProperties.setLevelEnum(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getServerCpuLevel()));
        // 服务器平均负载配置属性
        MonitoringServerLoadAverageProperties monitoringServerLoadAverageProperties = new MonitoringServerLoadAverageProperties();
        monitoringServerLoadAverageProperties.setEnable(monitorConfigPageFormVo.getServerLoadAverageEnable() == 1);
        monitoringServerLoadAverageProperties.setAlarmEnable(monitorConfigPageFormVo.getServerLoadAverageAlarmEnable() == 1);
        monitoringServerLoadAverageProperties.setOverloadThreshold15minutes(monitorConfigPageFormVo.getServerOverloadThreshold15minutes());
        monitoringServerLoadAverageProperties.setLevelEnum15minutes(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getServerOverloadLevel15minutes()));
        // 服务器磁盘配置属性
        MonitoringServerDiskProperties monitoringServerDiskProperties = new MonitoringServerDiskProperties();
        monitoringServerDiskProperties.setEnable(monitorConfigPageFormVo.getServerDiskEnable() == 1);
        monitoringServerDiskProperties.setAlarmEnable(monitorConfigPageFormVo.getServerDiskAlarmEnable() == 1);
        monitoringServerDiskProperties.setOverloadThreshold(monitorConfigPageFormVo.getServerDiskOverloadThreshold());
        monitoringServerDiskProperties.setLevelEnum(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getServerDiskLevel()));
        // 服务器内存配置属性
        MonitoringServerMemoryProperties monitoringServerMemoryProperties = new MonitoringServerMemoryProperties();
        monitoringServerMemoryProperties.setEnable(monitorConfigPageFormVo.getServerMemoryEnable() == 1);
        monitoringServerMemoryProperties.setAlarmEnable(monitorConfigPageFormVo.getServerMemoryAlarmEnable() == 1);
        monitoringServerMemoryProperties.setOverloadThreshold(monitorConfigPageFormVo.getServerMemoryOverloadThreshold());
        monitoringServerMemoryProperties.setLevelEnum(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getServerMemoryLevel()));
        // 服务器配置属性
        MonitoringServerProperties monitoringServerProperties = new MonitoringServerProperties();
        monitoringServerProperties.setEnable(monitorConfigPageFormVo.getServerEnable() == 1);
        monitoringServerProperties.setServerStatusProperties(monitoringServerStatusProperties);
        monitoringServerProperties.setServerCpuProperties(monitoringServerCpuProperties);
        monitoringServerProperties.setServerLoadAverageProperties(monitoringServerLoadAverageProperties);
        monitoringServerProperties.setServerDiskProperties(monitoringServerDiskProperties);
        monitoringServerProperties.setServerMemoryProperties(monitoringServerMemoryProperties);
        // 数据库状态配置属性
        MonitoringDbStatusProperties monitoringDbStatusProperties = new MonitoringDbStatusProperties();
        monitoringDbStatusProperties.setEnable(monitorConfigPageFormVo.getDbStatusEnable() == 1);
        monitoringDbStatusProperties.setAlarmEnable(monitorConfigPageFormVo.getDbStatusAlarmEnable() == 1);
        // 数据库表空间配置属性
        MonitoringDbTableSpaceProperties monitoringDbTableSpaceProperties = new MonitoringDbTableSpaceProperties();
        monitoringDbTableSpaceProperties.setEnable(monitorConfigPageFormVo.getDbTableSpaceEnable() == 1);
        monitoringDbTableSpaceProperties.setAlarmEnable(monitorConfigPageFormVo.getDbTableSpaceAlarmEnable() == 1);
        monitoringDbTableSpaceProperties.setOverloadThreshold(monitorConfigPageFormVo.getDbTableSpaceOverloadThreshold());
        monitoringDbTableSpaceProperties.setLevelEnum(AlarmLevelEnums.str2Enum(monitorConfigPageFormVo.getDbTableSpaceLevel()));
        // 数据库配置
        MonitoringDbProperties monitoringDbProperties = new MonitoringDbProperties();
        monitoringDbProperties.setEnable(monitorConfigPageFormVo.getDbEnable() == 1);
        monitoringDbProperties.setDbStatusProperties(monitoringDbStatusProperties);
        monitoringDbProperties.setDbTableSpaceProperties(monitoringDbTableSpaceProperties);
        // 监控配置属性
        MonitoringProperties properties = new MonitoringProperties();
        properties.setThreshold(monitorConfigPageFormVo.getThreshold());
        properties.setAlarmProperties(monitoringAlarmProperties);
        properties.setNetworkProperties(monitoringNetworkProperties);
        properties.setTcpProperties(monitoringTcpProperties);
        properties.setHttpProperties(monitoringHttpProperties);
        properties.setInstanceProperties(monitoringInstanceProperties);
        properties.setServerProperties(monitoringServerProperties);
        properties.setDbProperties(monitoringDbProperties);
        // 监控属性转json字符串
        String value = properties.toJsonString();
        // 查询数据库中是否有监控配置信息
        Integer selectCountDb = this.baseMapper.selectCount(new LambdaQueryWrapper<>());
        // 没有
        if (selectCountDb == null || selectCountDb == 0) {
            this.baseMapper.insert(MonitorConfig.builder().value(value).insertTime(new Date()).build());
        }
        // 有
        else {
            this.baseMapper.update(MonitorConfig.builder().value(value).updateTime(new Date()).build(), new LambdaUpdateWrapper<>());
        }
        // 刷新服务端监控属性配置
        try {
            BaseRequestPackage baseRequestPackage = this.uiPackageConstructor.structureBaseRequestPackage(null);
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
