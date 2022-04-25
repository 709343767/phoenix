package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorTcpHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorTcpHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.ITcpHistoryService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * TCP信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-03-16
 */
@Service
public class TcpHistoryServiceImpl extends ServiceImpl<IMonitorTcpHistoryDao, MonitorTcpHistory> implements ITcpHistoryService {

    /**
     * <p>
     * 清理TCP历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2022/4/25 11:29
     */
    @Override
    public int clearHistoryData(Date historyTime) {
        LambdaUpdateWrapper<MonitorTcpHistory> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.le(MonitorTcpHistory::getInsertTime, historyTime);
        return this.baseMapper.delete(lambdaUpdateWrapper);
    }

}
