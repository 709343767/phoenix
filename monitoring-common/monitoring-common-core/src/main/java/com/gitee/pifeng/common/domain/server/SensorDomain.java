package com.gitee.pifeng.common.domain.server;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 传感器信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 16:30
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SensorDomain extends AbstractSuperBean {

    /**
     * CPU温度（以摄氏度为单位）（如果可用）
     */
    private String cpuTemperature;

    /**
     * CPU电压（以伏特为单位）（如果可用）
     */
    private String cpuVoltage;

    /**
     * 风扇转速信息
     */
    private List<FanSpeedDomain> fanSpeedDomainList;

    @Data
    @ToString
    @NoArgsConstructor
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class FanSpeedDomain extends AbstractSuperBean {

        /**
         * 风扇的转速（rpm）（如果可用）
         */
        private String fanSpeed;
    }

}
