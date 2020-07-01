package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.server.business.web.dao.IMonitorUserDao;
import com.imby.server.business.web.entity.MonitorUser;
import com.imby.server.business.web.service.IMonitorUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 监控用户服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/1 17:39
 */
@Service
public class MonitorUserServiceImpl extends ServiceImpl<IMonitorUserDao, MonitorUser> implements IMonitorUserService {

}
