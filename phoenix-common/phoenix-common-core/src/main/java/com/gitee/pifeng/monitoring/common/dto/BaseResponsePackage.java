package com.gitee.pifeng.monitoring.common.dto;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperPackage;
import com.gitee.pifeng.monitoring.common.domain.Result;
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
public class BaseResponsePackage extends AbstractSuperPackage {

    /**
     * ID
     */
    protected String id;

    /**
     * 时间
     */
    protected Date dateTime;

    /**
     * 响应结果
     */
    protected Result result;

}
