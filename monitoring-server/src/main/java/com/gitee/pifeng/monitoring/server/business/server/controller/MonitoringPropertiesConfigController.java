package com.gitee.pifeng.monitoring.server.business.server.controller;

import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 监控属性配置控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/5 10:39
 */
@RestController
@RequestMapping("/monitoring-properties-config")
@Api(tags = "监控属性配置")
public class MonitoringPropertiesConfigController {

    /**
     * 监控配置属性加载器
     */
    @Autowired
    private MonitoringConfigPropertiesLoader monitoringConfigPropertiesLoader;

    /**
     * <p>
     * 刷新监控配置属性
     * </p>
     *
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2021/4/5 10:47
     */
    @ApiOperation(value = "刷新监控配置属性", notes = "刷新监控配置属性")
    @PostMapping("/refresh")
    public BaseResponsePackage refresh() {
        this.monitoringConfigPropertiesLoader.wakeUpMonitoringConfigPropertiesLoader();
        return new PackageConstructor().structureBaseResponsePackage(Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build());
    }

}
