package com.gitee.pifeng.common.dto;

import com.gitee.pifeng.common.abs.AbstractInstanceBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 心跳包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午12:20:06
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class HeartbeatPackage extends AbstractInstanceBean {

    /**
     * ID
     */
    private String id;

    /**
     * 时间
     */
    private Date dateTime;

    /**
     * 心跳频率
     */
    private long rate;

}
