package com.imby.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.imby.server.business.server.dao.*;
import com.imby.server.business.server.entity.*;
import com.imby.server.business.server.service.IMonitorConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 监控配置服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/4 11:06
 */
@Service
public class MonitorConfigServiceImpl implements IMonitorConfigService {

    /**
     * 监控配置数据访问对象
     */
    @Autowired
    private IMonitorConfigDao monitorConfigDao;

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
     * 监控网络配置数据访问对象
     */
    @Autowired
    private IMonitorConfigNetDao monitorConfigNetDao;

    /**
     * <p>
     * 加载所有监控配置
     * </p>
     *
     * @return 所有监控配置
     * @author 皮锋
     * @custom.date 2020/11/4 11:29
     */
    @Override
    public Map<String, Object> loadAllMonitorConfig() {
        MonitorConfig monitorConfig = this.monitorConfigDao.selectOne(new LambdaQueryWrapper<>());
        MonitorConfigNet monitorConfigNet = this.monitorConfigNetDao.selectOne(new LambdaQueryWrapper<>());
        MonitorConfigAlarm monitorConfigAlarm = this.monitorConfigAlarmDao.selectOne(new LambdaQueryWrapper<>());
        MonitorConfigAlarmMail monitorConfigAlarmMail = this.monitorConfigAlarmMailDao.selectOne(new LambdaQueryWrapper<>());
        MonitorConfigAlarmSms monitorConfigAlarmSms = this.monitorConfigAlarmSmsDao.selectOne(new LambdaQueryWrapper<>());
        Map<String, Object> map = new HashMap<>(16);
        map.put("monitorConfig", monitorConfig);
        map.put("monitorConfigNet", monitorConfigNet);
        map.put("monitorConfigAlarm", monitorConfigAlarm);
        map.put("monitorConfigAlarmMail", monitorConfigAlarmMail);
        map.put("monitorConfigAlarmSms", monitorConfigAlarmSms);
        return map;
    }

}
