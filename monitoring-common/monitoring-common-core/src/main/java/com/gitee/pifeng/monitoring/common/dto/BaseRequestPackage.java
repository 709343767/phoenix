package com.gitee.pifeng.monitoring.common.dto;

import com.gitee.pifeng.monitoring.common.abs.AbstractInstanceBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 基础请求包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/5 11:07
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseRequestPackage extends AbstractInstanceBean {

    /**
     * ID
     */
    protected String id;

    /**
     * 时间
     */
    protected Date dateTime;

}
