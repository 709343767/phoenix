package com.transfar.common.dto;

import com.transfar.common.abs.InstanceBean;
import com.transfar.common.domain.Result;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 基础响应包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月11日 上午9:40:59
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseResponsePackage extends InstanceBean implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8909786365854143972L;

    /**
     * ID
     */
    private String id;

    /**
     * 时间
     */
    private Date dateTime;

    /**
     * 响应结果
     */
    private Result result;

}
