package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorHttpHistoryDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorHttpHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IHttpHistoryService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * HTTP信息历史记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/13 9:07
 */
@Service
public class HttpHistoryServiceImpl extends ServiceImpl<IMonitorHttpHistoryDao, MonitorHttpHistory> implements IHttpHistoryService {
    /**
     * <p>
     * 清理HTTP历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2022/4/25 11:30
     */
    @Override
    public int clearHistoryData(Date historyTime) {
        LambdaUpdateWrapper<MonitorHttpHistory> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.le(MonitorHttpHistory::getInsertTime, historyTime);
        return this.baseMapper.delete(lambdaUpdateWrapper);
    }

}
