package com.gitee.pifeng.server.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.server.business.web.entity.MonitorServerNetcard;
import com.gitee.pifeng.server.business.web.vo.ServerDetailPageServerNetworkSpeedChartVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器网卡数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
public interface IMonitorServerNetcardDao extends BaseMapper<MonitorServerNetcard> {

    /**
     * <p>
     * 获取服务器详情页面服务器网卡信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器网卡
     * @author 皮锋
     * @custom.date 2021/1/9 20:23
     */
    List<MonitorServerNetcard> getServerDetailPageServerNetcardInfo(@Param("ip") String ip);

    /**
     * <p>
     * 获取服务器详情页面服务器网速图表信息
     * </p>
     *
     * @param params 请求参数
     * @return 服务器详情页面服务器网速图表信息
     * @author 皮锋
     * @custom.date 2021/1/10 20:48
     */
    List<ServerDetailPageServerNetworkSpeedChartVo> getServerDetailPageServerNetworkSpeedChartInfo(@Param("params") Map<String, Object> params);

    /**
     * <p>
     * 获取服务器网卡地址
     * </p>
     *
     * @param ip 服务器IP
     * @return 网卡地址列表
     * @author 皮锋
     * @custom.date 2021/1/11 9:54
     */
    List<String> getNetcardAddress(@Param("ip") String ip);

}
