package com.imby.server.business.server.domain;

import cn.hutool.core.util.NumberUtil;
import com.imby.common.abs.AbstractSuperBean;
import com.imby.common.domain.server.CpuDomain;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 服务器CPU
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/27 14:55
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Cpu extends AbstractSuperBean {

    /**
     * CPU过载次数
     */
    private int num;

    /**
     * 是否已经发送过CPU过载告警消息
     */
    private boolean isAlarm;

    /**
     * 是否确认CPU过载
     */
    private boolean isOverLoad;

    /**
     * 平均CPU使用率
     */
    private double avgCpuCombined;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 计算机名
     */
    private String computerName;

    /**
     * CPU信息
     */
    private CpuDomain cpuDomain;

    /**
     * <p>
     * 计算平均CPU使用率
     * </p>
     *
     * @param cpuDomain CPU信息
     * @return 平均CPU使用率
     * @author 皮锋
     * @custom.date 2020/3/27 15:45
     */
    public static double calculateAvgCpuCombined(CpuDomain cpuDomain) {
        List<CpuDomain.CpuInfoDomain> cpuInfoDomains = cpuDomain.getCpuList();
        // 集合对象为空
        if (CollectionUtils.isEmpty(cpuInfoDomains)) {
            return 0;
        }
        // 和
        double sum = 0;
        for (CpuDomain.CpuInfoDomain cpuInfoDomain : cpuInfoDomains) {
            double num = NumberUtil.round(cpuInfoDomain.getCpuCombined() * 100D, 2).doubleValue();
            sum += num;
        }
        double avg = sum / cpuInfoDomains.size();
        // 四舍五入保留两位小数
        return NumberUtil.round(avg, 2).doubleValue();
    }

}
