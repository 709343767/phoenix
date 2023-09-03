package com.gitee.pifeng.monitoring.ui.business.web.controller;

import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.service.IDbInfo4RedisService;
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
 * Redis数据库信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/16 20:17
 */
@RestController
@Tag(name = "数据库信息.Redis")
@RequestMapping("/db-info4redis")
public class DbInfo4RedisServiceController {

    /**
     * Redis数据库信息服务类
     */
    @Autowired
    private IDbInfo4RedisService dbInfo4RedisService;

    /**
     * <p>
     * 获取Redis信息
     * </p>
     *
     * @param id 数据库ID
     * @return layUiAdmin响应对象
     * @throws IOException    IO异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2021/10/16 20:50
     */
    @Operation(summary = "获取Redis信息")
    @Parameters(value = {
            @Parameter(name = "id", description = "数据库ID", required = true, in = ParameterIn.QUERY)})
    @GetMapping("/get-redis-info")
    @OperateLog(operModule = UiModuleConstants.DATABASE + "#信息", operType = OperateTypeConstants.QUERY, operDesc = "获取Redis信息")
    public LayUiAdminResultVo getRedisInfo(@RequestParam(value = "id") Long id) throws IOException, SigarException {
        String info = this.dbInfo4RedisService.getRedisInfo(id);
        return LayUiAdminResultVo.ok(info);
    }

}
