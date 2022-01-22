package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IDbInfo4MongoService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbInfo4MongoVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@Api(tags = "数据库信息.Mongo")
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
    @ApiOperation(value = "获取Mongo信息列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "id", value = "数据库ID", required = true, paramType = "query", dataType = "long")})
    @GetMapping("/get-mongo-info-list")
    @OperateLog(operModule = UiModuleConstants.DATABASE + "#信息", operType = OperateTypeConstants.QUERY, operDesc = "获取Mongo信息列表")
    public LayUiAdminResultVo getMongoInfoList(Long current, Long size, Long id) throws IOException, SigarException {
        Page<DbInfo4MongoVo> page = this.dbInfo4MongoService.getMongoInfoList(current, size, id);
        return LayUiAdminResultVo.ok(page);
    }

}
