package com.gitee.pifeng.common.dto;

import com.gitee.pifeng.common.abs.AbstractInstanceBean;
import com.gitee.pifeng.common.domain.Result;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 基础响应包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月11日 上午9:40:59
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseResponsePackage extends AbstractInstanceBean {

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