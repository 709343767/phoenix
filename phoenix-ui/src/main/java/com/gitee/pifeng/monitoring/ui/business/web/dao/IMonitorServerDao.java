package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServer;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
 */
public interface IMonitorServerDao extends BaseMapper<MonitorServer> {

    /**
     * <p>
     * 服务器在线率统计
     * </p>
     *
     * @return 服务器在线率统计信息
     * @author 皮锋
     * @custom.date 2021/5/12 20:37
     */
    Map<String, Object> getServerOnlineRateStatistics();

    /**
     * <p>
     * 获取服务器列表
     * </p>
     *
     * @param criteria 查询条件
     * @param ipage    分页Page对象接口
     * @return 服务器信息表现层对象
     * @author 皮锋
     * @custom.date 2021/10/13 14:48
     */
    IPage<MonitorServerVo> getMonitorServerList(IPage<MonitorServer> ipage, @Param("criteria") Map<String, Object> criteria);

    /**
     * <p>
     * 获取服务器列表
     * </p>
     *
     * @return 服务器信息表现层对象
     * @author 皮锋
     * @custom.date 2022/12/21 15:39
     */
    List<MonitorServerVo> getMonitorServers();

}
