package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控配置页面表单对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/6 11:16
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "监控配置页面表单对象")
public class MonitorConfigPageFormVo implements ISuperBean {

    /**
     * 监控阈值
     */
    @Schema(description = "监控阈值")
    private int threshold;

    /**
     * 是否监控网络
     */
    @Schema(description = "是否监控网络")
    private int netEnable;

    /**
     * 是否监控TCP
     */
    @Schema(description = "是否监控TCP")
    private int tcpEnable;

    /**
     * 是否监控HTTP
     */
    @Schema(description = "是否监控HTTP")
    private int httpEnable;

    /**
     * 监控告警是否打开
     */
    @Schema(description = "监控告警是否打开")
    private int alarmEnable;

    /**
     * 监控告警级别
     */
    @Schema(description = "监控告警级别")
    private String alarmLevel;

    /**
     * 监控告警方式
     */
    @Schema(description = "监控告警方式")
    private String[] alarmWay;

    /**
     * 告警收件人邮箱地址，多个地址用英文分号隔开
     */
    @Schema(description = "告警收件人邮箱地址，多个地址用英文分号隔开")
    private String alarmMailEmills;

    /**
     * 告警短信接口地址
     */
    @Schema(description = "告警短信接口地址")
    private String alarmSmsAddress;

    /**
     * 短信接口企业
     */
    @Schema(description = "短信接口企业")
    private String alarmSmsEnterprise;

    /**
     * 告警短信手机号码，多个号码用英文分号隔开
     */
    @Schema(description = "告警短信手机号码，多个号码用英文分号隔开")
    private String alarmSmsPhoneNumbers;

    /**
     * 告警短信接口协议
     */
    @Schema(description = "告警短信接口协议")
    private String alarmSmsProtocol;

    /**
     * 是否监控服务器
     */
    @Schema(description = "是否监控服务器")
    private int serverEnable;

    /**
     * CPU过载阈值
     */
    @Schema(description = "CPU过载阈值")
    private double serverCpuOverloadThreshold;

    /**
     * CPU监控级别
     */
    @Schema(description = "CPU监控级别")
    private String serverCpuLevel;

    /**
     * 服务器15分钟过载阈值
     */
    @Schema(description = "服务器15分钟过载阈值")
    private double serverOverloadThreshold15minutes;

    /**
     * 服务器15分钟过载监控级别
     */
    @Schema(description = "服务器15分钟过载监控级别")
    private String serverOverloadLevel15minutes;

    /**
     * 磁盘过载阈值
     */
    @Schema(description = "磁盘过载阈值")
    private double serverDiskOverloadThreshold;

    /**
     * 磁盘监控级别
     */
    @Schema(description = "磁盘监控级别")
    private String serverDiskLevel;

    /**
     * 内存过载阈值
     */
    @Schema(description = "内存过载阈值")
    private double serverMemoryOverloadThreshold;

    /**
     * 内存监控级别
     */
    @Schema(description = "内存监控级别")
    private String serverMemoryLevel;

    /**
     * 是否监控数据库
     */
    @Schema(description = "是否监控数据库")
    private int dbEnable;

    /**
     * 数据库表空间过载阈值
     */
    @Schema(description = "数据库表空间过载阈值")
    private double dbTableSpaceOverloadThreshold;

    /**
     * 数据库表空间监控级别
     */
    @Schema(description = "数据库表空间监控级别")
    private String dbTableSpaceLevel;

}
