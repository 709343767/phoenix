package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorLogExceptionDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorLogException;
import com.gitee.pifeng.monitoring.server.business.server.service.ILogExceptionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 异常日志服务层接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-06-09
 */
@Service
public class LogExceptionServiceImpl extends ServiceImpl<IMonitorLogExceptionDao, MonitorLogException> implements ILogExceptionService {

}
