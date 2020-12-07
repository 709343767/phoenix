package com.gitee.pifeng.server.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.server.business.web.entity.MonitorServerDisk;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务器磁盘数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
public interface IMonitorServerDiskDao extends BaseMapper<MonitorServerDisk> {

    /**
     * <p>
     * 获取服务器详情页面服务器磁盘图表信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器磁盘表
     * @author 皮锋
     * @custom.date 2020/10/22 18:03
     */
    List<MonitorServerDisk> getServerDetailPageServerDiskChartInfo(@Param("ip") String ip);

}
