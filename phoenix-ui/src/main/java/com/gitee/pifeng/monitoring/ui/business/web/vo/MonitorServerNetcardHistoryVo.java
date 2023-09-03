package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerNetcardHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器网卡历史记录表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-25
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "服务器网卡历史记录表现层对象")
public class MonitorServerNetcardHistoryVo implements ISuperBean {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "网卡序号")
    private Integer netNo;

    @Schema(description = "网卡名字")
    private String name;

    @Schema(description = "网卡类型")
    private String type;

    @Schema(description = "网卡地址")
    private String address;

    @Schema(description = "子网掩码")
    private String mask;

    @Schema(description = "广播地址")
    private String broadcast;

    @Schema(description = "MAC地址")
    private String hwAddr;

    @Schema(description = "网卡信息描述")
    private String description;

    @Schema(description = "接收到的总字节数")
    private Long rxBytes;

    @Schema(description = "接收的总包数")
    private Long rxPackets;

    @Schema(description = "接收到的错误包数")
    private Long rxErrors;

    @Schema(description = "接收时丢弃的包数")
    private Long rxDropped;

    @Schema(description = "发送的总字节数")
    private Long txBytes;

    @Schema(description = "发送的总包数")
    private Long txPackets;

    @Schema(description = "发送时的错误包数")
    private Long txErrors;

    @Schema(description = "发送时丢弃的包数")
    private Long txDropped;

    @Schema(description = "下载速度")
    private Double downloadBps;

    @Schema(description = "上传速度")
    private Double uploadBps;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerNetcardHistoryVo转MonitorServerNetcardHistory
     * </p>
     *
     * @return {@link MonitorServerNetcardHistory}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerNetcardHistory convertTo() {
        MonitorServerNetcardHistory monitorServerNetcardHistory = MonitorServerNetcardHistory.builder().build();
        BeanUtils.copyProperties(this, monitorServerNetcardHistory);
        return monitorServerNetcardHistory;
    }

    /**
     * <p>
     * MonitorServerNetcardHistory转MonitorServerNetcardHistoryVo
     * </p>
     *
     * @param monitorServerNetcardHistory {@link MonitorServerNetcardHistory}
     * @return {@link MonitorServerNetcardHistoryVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerNetcardHistoryVo convertFor(MonitorServerNetcardHistory monitorServerNetcardHistory) {
        if (null != monitorServerNetcardHistory) {
            BeanUtils.copyProperties(monitorServerNetcardHistory, this);
        }
        return this;
    }

}
