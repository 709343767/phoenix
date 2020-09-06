package com.imby.server.business.web.service.impl;

import com.imby.server.business.web.entity.MonitorJvmRuntime;
import com.imby.server.business.web.dao.IMonitorJvmRuntimeDao;
import com.imby.server.business.web.service.IMonitorJvmRuntimeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * java虚拟机运行时信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Service
public class MonitorJvmRuntimeServiceImpl extends ServiceImpl<IMonitorJvmRuntimeDao, MonitorJvmRuntime> implements IMonitorJvmRuntimeService {

}
