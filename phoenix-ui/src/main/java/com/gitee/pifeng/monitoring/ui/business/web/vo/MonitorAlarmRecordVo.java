package com.gitee.pifeng.monitoring.ui.business.web.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 监控告警表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/5 14:49
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "监控告警表现层对象")
public class MonitorAlarmRecordVo implements ISuperBean {

    @JsonSerialize(using = ToStringSerializer.class)
    @Excel(name = "ID", orderNum = "1")
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "告警代码，使用UUID")
    private String code;

    @Schema(description = "告警定义编码")
    private String alarmDefCode;

    @Excel(name = "告警类型", orderNum = "2",
            replace = {"服务器_SERVER", "网络_NET", "TCP服务_TCP4SERVICE", "HTTP服务_HTTP4SERVICE", "docker_DOCKER", "应用_INSTANCE", "数据库_DATABASE", "自定义_CUSTOM"})
    @Schema(description = "告警类型（SERVER、NET、TCP4SERVICE、HTTP4SERVICE、DOCKER、INSTANCE、DATABASE、CUSTOM）")
    private String type;

    @Excel(name = "告警方式", orderNum = "3", replace = {"短信_SMS", "邮件_MAIL", "钉钉_DINGTALK", "企业微信_EPWECHAT"})
    @Schema(description = "告警方式（SMS、MAIL、...）")
    private String way;

    @Excel(name = "告警级别", orderNum = "4", replace = {"消息_INFO", "警告_WARM", "错误_ERROR", "严重_FATAL"})
    @Schema(description = "告警级别（INFO、WARM、ERROR、FATAL）")
    private String level;

    @Excel(name = "告警标题", orderNum = "5")
    @Schema(description = "告警标题")
    private String title;

    @Excel(name = "告警内容", orderNum = "6")
    @Schema(description = "告警内容")
    private String content;

    @Excel(name = "状态", orderNum = "7", replace = {"失败_0", "成功_1", "不提醒_null"})
    @Schema(description = "告警发送状态（0：失败；1：成功）")
    private String status;

    @Excel(name = "号码", orderNum = "8")
    @Schema(description = "被告警人号码（手机号码、电子邮箱、...）")
    private String number;

    @Excel(name = "不提醒原因", orderNum = "9")
    @Schema(description = "不发送告警原因")
    private String notSendReason;

    @Excel(name = "记录时间", orderNum = "10", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "告警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @Excel(name = "告警时间", orderNum = "11", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "告警结果获取时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * <p>
     * MonitorAlarmRecordVo转MonitorAlarmRecord
     * </p>
     *
     * @return {@link MonitorAlarmRecord}
     * @author 皮锋
     * @custom.date 2020/7/14 10:31
     */
    public MonitorAlarmRecord convertTo() {
        MonitorAlarmRecord monitorAlarmRecord = MonitorAlarmRecord.builder().build();
        BeanUtils.copyProperties(this, monitorAlarmRecord);
        return monitorAlarmRecord;
    }

    /**
     * <p>
     * MonitorAlarmRecord转MonitorAlarmRecordVo
     * </p>
     *
     * @param monitorAlarmRecord {@link MonitorAlarmRecord}
     * @return {@link MonitorAlarmRecordVo}
     * @author 皮锋
     * @custom.date 2020/7/14 10:34
     */
    public MonitorAlarmRecordVo convertFor(MonitorAlarmRecord monitorAlarmRecord) {
        if (null != monitorAlarmRecord) {
            BeanUtils.copyProperties(monitorAlarmRecord, this);
        }
        return this;
    }

}
