package com.imby.server.business.web.controller;


import com.imby.server.business.web.service.IMonitorServerDiskService;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.ServerDetailPageServerDiskVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 服务器磁盘
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@RestController
@RequestMapping("/monitor-server-disk")
public class MonitorServerDiskController {

    /**
     * 服务器磁盘服务类
     */
    @Autowired
    private IMonitorServerDiskService monitorServerDiskService;

    /**
     * <p>
     * 获取服务器详情页面服务器磁盘信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/10/22 17:26
     */
    @ApiOperation(value = "获取服务器详情页面服务器磁盘信息")
    @ResponseBody
    @GetMapping("/get-server-detail-page-server-disk-info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ip", value = "服务器IP地址", required = true, paramType = "query", dataType = "string")})
    public LayUiAdminResultVo getServerDetailPageServerDisk(@RequestParam(name = "ip") String ip) {
        List<ServerDetailPageServerDiskVo> serverDetailPageServerDiskVos = this.monitorServerDiskService.getServerDetailPageServerDisk(ip);
        return LayUiAdminResultVo.ok(serverDetailPageServerDiskVos);
    }

}

