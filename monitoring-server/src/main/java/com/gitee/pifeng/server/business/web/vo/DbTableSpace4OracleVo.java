package com.gitee.pifeng.server.business.web.vo;

import com.gitee.pifeng.common.inf.ISuperBean;
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
 * @custom.date 2020/12/31 16:20
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
public class DbTableSpace4OracleVo implements ISuperBean {

    @ApiModelProperty(value = "文件ID")
    private Long fileId;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "表空间")
    private String tablespaceName;

    @ApiModelProperty(value = "表空间大小")
    private String total;

    @ApiModelProperty(value = "表空间使用大小")
    private String used;

    @ApiModelProperty(value = "表空间剩余大小")
    private String free;

    @ApiModelProperty(value = "使用百分比")
    private Double usedPer;

    @ApiModelProperty(value = "剩余百分比")
    private Double freePer;

}
