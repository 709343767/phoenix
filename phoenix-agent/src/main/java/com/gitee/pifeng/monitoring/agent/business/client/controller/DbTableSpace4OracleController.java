package com.gitee.pifeng.monitoring.agent.business.client.controller;

import com.gitee.pifeng.monitoring.agent.business.client.service.IBaseRequestPackageService;
import com.gitee.pifeng.monitoring.agent.constant.UrlConstants;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Oracle数据库表空间控制器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/31 16:08
 */
@Slf4j
@RestController
@Tag(name = "数据库表空间.Oracle")
@RequestMapping("/db-tablespace4oracle")
public class DbTableSpace4OracleController {

    /**
     * 基础请求包服务接口
     */
    @Autowired
    private IBaseRequestPackageService baseRequestPackageService;

    /**
     * <p>
     * 获取表空间列表(按文件)
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020/12/24 16:53
     */
    @Operation(summary = "获取表空间列表(按文件)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CiphertextPackage.class))),
            responses = @ApiResponse(content = {@Content(schema = @Schema(implementation = CiphertextPackage.class))}))
    @PostMapping("/get-tablespace-list-file")
    public BaseResponsePackage getTableSpaceListFile(@RequestBody BaseRequestPackage baseRequestPackage) {
        return this.baseRequestPackageService.dealBaseRequestPackage(baseRequestPackage, UrlConstants.ORACLE_GET_TABLESPACE_LIST_FILE_URL);
    }

    /**
     * <p>
     * 获取表空间列表
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020/12/24 16:53
     */
    @Operation(summary = "获取表空间列表",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CiphertextPackage.class))),
            responses = @ApiResponse(content = {@Content(schema = @Schema(implementation = CiphertextPackage.class))}))
    @PostMapping("/get-tablespace-list-all")
    public BaseResponsePackage getTableSpaceListAll(@RequestBody BaseRequestPackage baseRequestPackage) {
        return this.baseRequestPackageService.dealBaseRequestPackage(baseRequestPackage, UrlConstants.ORACLE_GET_TABLESPACE_LIST_ALL_URL);
    }

}
