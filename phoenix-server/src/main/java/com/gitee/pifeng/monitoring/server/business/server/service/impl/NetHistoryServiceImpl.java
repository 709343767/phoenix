package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorNetHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNetHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.INetHistoryService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 网络信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-20
 */
@Service
public class NetHistoryServiceImpl extends ServiceImpl<IMonitorNetHistoryDao, MonitorNetHistory> implements INetHistoryService {

    /**
     * <p>
     * 清理网络历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2022/4/25 11:20
     */
    // @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public int clearHistoryData(Date historyTime) {
        LambdaQueryWrapper<MonitorNetHistory> netHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        netHistoryLambdaQueryWrapper.le(MonitorNetHistory::getInsertTime, historyTime);
        netHistoryLambdaQueryWrapper.orderByAsc(MonitorNetHistory::getInsertTime);
        netHistoryLambdaQueryWrapper.last("limit 5000");
        return this.baseMapper.delete(netHistoryLambdaQueryWrapper);
    }

}
