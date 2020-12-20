package com.gitee.pifeng.server.business.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.server.business.server.dao.IMonitorDbDao;
import com.gitee.pifeng.server.business.server.entity.MonitorDb;
import com.gitee.pifeng.server.business.server.service.IMonitorDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据库表服务接口实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-12-19
 */
@Service
public class MonitorDbServiceImpl extends ServiceImpl<IMonitorDbDao, MonitorDb> implements IMonitorDbService {

    /**
     * 数据库表数据访问对象
     */
    @Autowired
    private IMonitorDbDao monitorDbDao;

    /**
     * <p>
     * 更新数据库信息
     * </p>
     *
     * @param monitorDb 数据库
     * @return 更新记录数
     * @author 皮锋
     * @custom.date 2020/11/23 11:05
     */
    @Override
    public int updateMonitorDb(MonitorDb monitorDb) {
        return this.monitorDbDao.updateById(monitorDb);
    }

}