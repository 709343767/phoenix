package com.transfar.server.business.server.domain;

import com.transfar.common.abs.SuperBean;
import com.transfar.common.domain.server.MemoryDomain;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 服务器内存
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/26 14:53
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Memory extends SuperBean {

    /**
     * 内存过载次数
     */
    private int num;

    /**
     * 是否已经发送过内存过载告警消息
     */
    private boolean isAlarm;

    /**
     * 是否确认内存过载
     */
    private boolean isOverLoad;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 计算机名
     */
    private String computerName;

    /**
     * 内存信息
     */
    private MemoryDomain memoryDomain;

    /**
     * 内存使用率
     */
    private double usedPercent;

    /**
     * <p>
     * 计算内存使用率
     * </p>
     *
     * @param menUsedPercent 内存使用率字符串
     * @return 内存使用率
     * @author 皮锋
     * @custom.date 2020/3/31 12:49
     */
    public static double calculateUsePercent(String menUsedPercent) {
        if (StringUtils.isBlank(menUsedPercent)) {
            return 0;
        }
        return Double.parseDouble(menUsedPercent.substring(0, menUsedPercent.length() - 1));
    }

}
