package com.imby.server.business.web.service.impl;

import com.imby.server.business.web.entity.MonitorJvmThread;
import com.imby.server.business.web.dao.IMonitorJvmThreadDao;
import com.imby.server.business.web.service.IMonitorJvmThreadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机线程信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Service
public class MonitorJvmThreadServiceImpl extends ServiceImpl<IMonitorJvmThreadDao, MonitorJvmThread> implements IMonitorJvmThreadService {

}
