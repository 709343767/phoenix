package com.gitee.pifeng.common.dto;

import com.gitee.pifeng.common.abs.AbstractInstanceBean;
import com.gitee.pifeng.common.domain.Alarm;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 监控告警信息包
 * </p>
 * 用来定义监控告警信息的数据格式
 *
 * @author 皮锋
 * @custom.date 2020/3/3 10:01
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AlarmPackage extends AbstractInstanceBean {

    /**
     * ID
     */
    private String id;

    /**
     * 告警对象
     */
    private Alarm alarm;

    /**
     * 时间
     */
    private Date dateTime;

}
