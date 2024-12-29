package com.gitee.pifeng.monitoring.common.domain;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <p>
 * 许可证信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/5/3 17:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class License extends AbstractSuperBean {

    /**
     * 截止时间
     */
    private LocalDateTime deadline;

}
