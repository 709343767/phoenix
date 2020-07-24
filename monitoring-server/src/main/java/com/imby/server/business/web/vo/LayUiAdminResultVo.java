package com.imby.server.business.web.vo;

import com.imby.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * <p>
 * LayUiAdmin响应对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/7/23 17:36
 */
@Getter
@Setter
@ToString
@ApiModel(value = "LayUiAdmin响应对象")
public class LayUiAdminResultVo implements ISuperBean {

    /**
     * 接口状态
     */
    @ApiModelProperty(value = "接口状态")
    private int code;

    /**
     * 提示信息
     */
    @ApiModelProperty(value = "提示信息")
    private String msg;

    /**
     * 数据
     */
    @ApiModelProperty(value = "数据")
    private Object data;

    /**
     * <p>
     * 构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/7/23 18:27
     */
    public LayUiAdminResultVo() {
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
        this.data = null;
    }

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
        LayUiAdminResultVo layUiAdminResultVo = new LayUiAdminResultVo();
        layUiAdminResultVo.setData(data);
        return layUiAdminResultVo;
    }

    /**
     * <p>
     * 创建LayUiAdmin成功响应对象
     * </p>
     *
     * @return LayUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/7/23 18:26
     */
    public static LayUiAdminResultVo ok() {
        return new LayUiAdminResultVo();
    }
}
