package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.common.threadpool.ThreadPool;
import com.imby.server.business.web.dao.*;
import com.imby.server.business.web.entity.*;
import com.imby.server.business.web.service.IMonitorConfigService;
import com.imby.server.business.web.vo.MonitorConfigPageFormVo;
import com.imby.server.inf.IMonitorConfigListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 监控配置服务实现类
 * </p>
 *
 * @author 皮锋
 * @since 2020-11-04
 */
@Service
public class MonitorConfigServiceImpl extends ServiceImpl<IMonitorConfigDao, MonitorConfig> implements IMonitorConfigService {

    /**
     * 监控配置数据访问对象
     */
    @Autowired
    private IMonitorConfigDao monitorConfigDao;

    /**
     * 监控网络配置数据访问对象
     */
    @Autowired
    private IMonitorConfigNetDao monitorConfigNetDao;

    /**
     * 监控告警配置数据访问对象
     */
    @Autowired
    private IMonitorConfigAlarmDao monitorConfigAlarmDao;

    /**
     * 监控邮件告警配置数据访问对象
     */
    @Autowired
    private IMonitorConfigAlarmMailDao monitorConfigAlarmMailDao;

    /**
     * 监控短信告警配置数据访问对象
     */
    @Autowired
    private IMonitorConfigAlarmSmsDao monitorConfigAlarmSmsDao;

    /**
     * 监控服务器信息配置数据访问对象
     */
    @Autowired
    private IMonitorConfigServerDao monitorConfigServerDao;

    /**
     * 监控服务器CPU信息配置数据访问对象
     */
    @Autowired
    private IMonitorConfigServerCpuDao monitorConfigServerCpuDao;

    /**
     * 监控服务器磁盘信息配置数据访问对象
     */
    @Autowired
    private IMonitorConfigServerDiskDao monitorConfigServerDiskDao;

    /**
     * 监控服务器内存信息配置数据访问对象
     */
    @Autowired
    private IMonitorConfigServerMemoryDao monitorConfigServerMemoryDao;

    /**
     * 监控配置监听器
     */
    @Autowired
    private List<IMonitorConfigListener> monitorConfigListeners;

    /**
     * <p>
     * 更新监控配置
     * </p>
     *
     * @param monitorConfigPageFormVo 监控配置页面表单对象
     * @return 是否更新成功
     * @author 皮锋
     * @custom.date 2020/11/9 20:11
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean update(MonitorConfigPageFormVo monitorConfigPageFormVo) {
        //更新时间
        Date updateTime = new Date();
        this.monitorConfigDao.update(MonitorConfig.builder()
                .threshold(monitorConfigPageFormVo.getThreshold())
                .updateTime(updateTime)
                .build(), new UpdateWrapper<>());
        this.monitorConfigNetDao.update(MonitorConfigNet.builder()
                .enable(monitorConfigPageFormVo.getNetEnable())
                .updateTime(updateTime)
                .build(), new UpdateWrapper<>());
        this.monitorConfigAlarmDao.update(MonitorConfigAlarm.builder()
                .enable(monitorConfigPageFormVo.getAlarmEnable())
                .level(monitorConfigPageFormVo.getAlarmLevel())
                .way(monitorConfigPageFormVo.getAlarmWay())
                .updateTime(updateTime)
                .build(), new UpdateWrapper<>());
        this.monitorConfigAlarmMailDao.update(MonitorConfigAlarmMail.builder()
                .emills(monitorConfigPageFormVo.getAlarmMailEmills())
                .updateTime(updateTime)
                .build(), new UpdateWrapper<>());
        this.monitorConfigAlarmSmsDao.update(MonitorConfigAlarmSms.builder()
                .address(monitorConfigPageFormVo.getAlarmSmsAddress())
                .enterprise(monitorConfigPageFormVo.getAlarmSmsEnterprise())
                .phoneNumbers(monitorConfigPageFormVo.getAlarmSmsPhoneNumbers())
                .protocol(monitorConfigPageFormVo.getAlarmSmsProtocol())
                .updateTime(updateTime)
                .build(), new UpdateWrapper<>());
        this.monitorConfigServerDao.update(MonitorConfigServer.builder()
                .enable(monitorConfigPageFormVo.getServerEnable())
                .updateTime(updateTime)
                .build(), new UpdateWrapper<>());
        this.monitorConfigServerCpuDao.update(MonitorConfigServerCpu.builder()
                .overloadThreshold(monitorConfigPageFormVo.getServerCpuOverloadThreshold())
                .updateTime(updateTime)
                .build(), new UpdateWrapper<>());
        this.monitorConfigServerDiskDao.update(MonitorConfigServerDisk.builder()
                .overloadThreshold(monitorConfigPageFormVo.getServerDiskOverloadThreshold())
                .updateTime(updateTime)
                .build(), new UpdateWrapper<>());
        this.monitorConfigServerMemoryDao.update(MonitorConfigServerMemory.builder()
                .overloadThreshold(monitorConfigPageFormVo.getServerMemoryOverloadThreshold())
                .updateTime(updateTime)
                .build(), new UpdateWrapper<>());
        // 调用监听器回调接口
        this.monitorConfigListeners.forEach(e ->
                ThreadPool.COMMON_CPU_INTENSIVE_THREAD_POOL.execute(e::wakeUpMonitoringConfigPropertiesLoader));
        return true;
    }

}
