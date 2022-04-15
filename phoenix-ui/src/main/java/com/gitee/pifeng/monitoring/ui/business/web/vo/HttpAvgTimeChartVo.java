package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * HTTP访问耗时图表信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/3/17 21:42
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "HTTP访问耗时图表信息表现层对象")
public class HttpAvgTimeChartVo implements ISuperBean {

    @ApiModelProperty(value = "所有列表")
    private List<All> allList;

    @ApiModelProperty(value = "异常列表")
    private List<Exc> excList;

    /**
     * <p>
     * 所有
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/3/19 15:45
     */
    @ApiModel(value = "所有")
    @Data
    public static class All {

        @ApiModelProperty(value = "平均响应时间（毫秒）")
        private Long avgTime;

        @ApiModelProperty(value = "状态码")
        private Integer status;

        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
        @ApiModelProperty(value = "新增时间")
        private Date insertTime;
    }

    /**
     * <p>
     * 异常
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/3/19 15:46
     */
    @ApiModel(value = "异常")
    @Data
    public static class Exc {

        @ApiModelProperty(value = "平均响应时间（毫秒）")
        private Long avgTime;

        @ApiModelProperty(value = "状态码")
        private Integer status;

        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
        @ApiModelProperty(value = "新增时间")
        private Date insertTime;
    }

}
