package com.gitee.pifeng.monitoring.common.dto;

import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 下线信息包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/5/30 13:32
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class OfflinePackage extends BaseRequestPackage {

    /**
     * 离线类型
     */
    private List<MonitorTypeEnums> monitorTypes;

}
