package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IDbInfo4MongoService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbInfo4MongoVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * <p>
 * Mongo数据库信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/20 14:22
 */
@RestController
@Tag(name = "数据库信息.Mongo")
@RequestMapping("/db-info4mongo")
public class DbInfo4MongoServiceController {

    /**
     * Mongo数据库信息服务类
     */
    @Autowired
    private IDbInfo4MongoService dbInfo4MongoService;

    /**
     * <p>
     * 获取Mongo信息列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return layUiAdmin响应对象
     * @throws IOException    IO异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2022/1/20 14:28
     */
    @Operation(summary = "获取Mongo信息列表")
    @Parameters(value = {
            @Parameter(name = "current", description = "当前页", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "size", description = "每页显示条数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "id", description = "数据库ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/get-mongo-info-list")
    @OperateLog(operModule = UiModuleConstants.DATABASE + "#信息", operType = OperateTypeConstants.QUERY, operDesc = "获取Mongo信息列表")
    public LayUiAdminResultVo getMongoInfoList(@RequestParam(value = "current") Long current,
                                               @RequestParam(value = "size") Long size,
                                               @RequestParam(value = "id") Long id) throws IOException, SigarException {
        Page<DbInfo4MongoVo> page = this.dbInfo4MongoService.getMongoInfoList(current, size, id);
        return LayUiAdminResultVo.ok(page);
    }

}
