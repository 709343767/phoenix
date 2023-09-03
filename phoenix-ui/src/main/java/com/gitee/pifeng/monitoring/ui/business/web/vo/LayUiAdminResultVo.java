package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * <p>
 * LayUiAdmin响应对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/23 17:36
 */
@Data
@AllArgsConstructor
@Schema(description = "LayUiAdmin响应对象")
public class LayUiAdminResultVo implements ISuperBean {

    /**
     * 接口状态码
     */
    @Schema(description = "接口状态码")
    private int code;

    /**
     * 接口提示信息
     */
    @Schema(description = "接口提示信息")
    private String msg;

    /**
     * 数据
     */
    @Schema(description = "数据")
    private Object data;

    /**
     * <p>
     * 创建LayUiAdmin成功响应对象
     * </p>
     *
     * @param data 数据
     * @return LayUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/7/23 18:25
     */
    public static LayUiAdminResultVo ok(Object data) {
        return new LayUiAdminResultVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * <p>
     * 创建LayUiAdmin失败响应对象
     * </p>
     *
     * @param exp 异常信息
     * @return LayUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/1/28 11:49
     */
    public static LayUiAdminResultVo fail(Object exp) {
        return LayUiAdminResultVo.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exp);
    }

    /**
     * <p>
     * 创建LayUiAdmin失败响应对象
     * </p>
     *
     * @param code 接口状态
     * @param msg  接口提示信息
     * @param exp  异常信息
     * @return LayUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/1/28 11:50
     */
    public static LayUiAdminResultVo fail(int code, String msg, Object exp) {
        return new LayUiAdminResultVo(code, msg, exp);
    }

}
