package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * Oracle数据库表空间表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/5 14:34
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Oracle数据库表空间表现层对象")
public class DbTableSpaceAll4OracleVo implements ISuperBean {

    @ApiModelProperty(value = "表空间")
    private String tablespaceName;

    @ApiModelProperty(value = "总空间")
    private String total;

    @ApiModelProperty(value = "使用空间")
    private String used;

    @ApiModelProperty(value = "剩余空间")
    private String free;

    @ApiModelProperty(value = "表空间使用率")
    private Double usedRate;

    @ApiModelProperty(value = "表空间剩余率")
    private Double freeRate;

}
