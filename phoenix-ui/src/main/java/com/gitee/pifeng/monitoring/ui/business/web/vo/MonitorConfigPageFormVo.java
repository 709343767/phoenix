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
     * 是否监控网络状态
     */
    @Schema(description = "是否监控网络状态")
    private int netStatusEnable;

    /**
     * 网络状态监控告警是否打开
     */
    @Schema(description = "网络状态监控告警是否打开")
    private int netStatusAlarmEnable;

    /**
     * 是否监控TCP
     */
    @Schema(description = "是否监控TCP")
    private int tcpEnable;

    /**
     * 是否监控TCP状态
     */
    @Schema(description = "是否监控TCP状态")
    private int tcpStatusEnable;

    /**
     * TCP状态监控告警是否打开
     */
    @Schema(description = "TCP状态监控告警是否打开")
    private int tcpStatusAlarmEnable;

    /**
     * 是否监控HTTP
     */
    @Schema(description = "是否监控HTTP")
    private int httpEnable;

    /**
     * 是否监控HTTP状态
     */
    @Schema(description = "是否监控HTTP状态")
    private int httpStatusEnable;

    /**
     * HTTP状态监控告警是否打开
     */
    @Schema(description = "HTTP状态监控告警是否打开")
    private int httpStatusAlarmEnable;

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
     * 是否开启告警静默
     */
    @Schema(description = "是否开启告警静默")
    private int alarmSilenceEnable;

    /**
     * 告警静默时间段
     */
    @Schema(description = "告警静默时间段")
    private String alarmSilenceTimeSlot;

    /**
     * 监控告警方式
     */
    @Schema(description = "监控告警方式")
    private String[] alarmWay;

    /**
     * 告警收件人邮箱地址，多个地址用英文分号隔开
     */
    @Schema(description = "告警收件人邮箱地址，多个地址用英文分号隔开")
    private String alarmMailboxEmails;

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
     * 是否监控应用实例
     */
    @Schema(description = "是否监控应用实例")
    private int instanceEnable;

    /**
     * 是否监控应用实例状态
     */
    @Schema(description = "是否监控应用实例状态")
    private int instanceStatusEnable;

    /**
     * 应用实例状态监控告警是否打开
     */
    @Schema(description = "应用实例状态监控告警是否打开")
    private int instanceStatusAlarmEnable;

    /**
     * 是否监控服务器
     */
    @Schema(description = "是否监控服务器")
    private int serverEnable;

    /**
     * 是否监控服务器状态
     */
    @Schema(description = "是否监控服务器状态")
    private int serverStatusEnable;

    /**
     * 服务器状态监控告警是否打开
     */
    @Schema(description = "服务器状态监控告警是否打开")
    private int serverStatusAlarmEnable;

    /**
     * 是否监控服务器CPU
     */
    @Schema(description = "是否监控服务器CPU")
    private int serverCpuEnable;

    /**
     * 服务器CPU监控告警是否打开
     */
    @Schema(description = "服务器CPU监控告警是否打开")
    private int serverCpuAlarmEnable;

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
     * 是否监控服务器平均负载
     */
    @Schema(description = "是否监控服务器平均负载")
    private int serverLoadAverageEnable;

    /**
     * 服务器平均负载监控告警是否打开
     */
    @Schema(description = "服务器平均负载监控告警是否打开")
    private int serverLoadAverageAlarmEnable;

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
     * 是否监控服务器磁盘
     */
    @Schema(description = "是否监控服务器磁盘")
    private int serverDiskEnable;

    /**
     * 服务器磁盘监控告警是否打开
     */
    @Schema(description = "服务器磁盘监控告警是否打开")
    private int serverDiskAlarmEnable;

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
     * 是否监控服务器内存
     */
    @Schema(description = "是否监控服务器内存")
    private int serverMemoryEnable;

    /**
     * 服务器内存监控告警是否打开
     */
    @Schema(description = "服务器内存监控告警是否打开")
    private int serverMemoryAlarmEnable;

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
     * 是否监控数据库状态
     */
    @Schema(description = "是否监控数据库状态")
    private int dbStatusEnable;

    /**
     * 数据库状态监控告警是否打开
     */
    @Schema(description = "数据库状态监控告警是否打开")
    private int dbStatusAlarmEnable;

    /**
     * 是否监控数据库表空间
     */
    @Schema(description = "是否监控数据库表空间")
    private int dbTableSpaceEnable;

    /**
     * 数据库表空间监控告警是否打开
     */
    @Schema(description = "数据库表空间监控告警是否打开")
    private int dbTableSpaceAlarmEnable;

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
