package com.transfar.dto;

import com.transfar.common.InstanceBean;
import com.transfar.domain.server.ServerDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 服务器信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午9:54:41
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ServerPackage extends InstanceBean {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 服务器信息
     */
    @ApiModelProperty(value = "服务器信息")
    private ServerDomain serverDomain;

    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    private Date dateTime;

}
