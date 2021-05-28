package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerDiskHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器磁盘历史记录表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-01-24
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器磁盘历史记录表现层对象")
public class MonitorServerDiskHistoryVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "磁盘序号")
    @TableField("DISK_NO")
    private Integer diskNo;

    @ApiModelProperty(value = "分区的盘符名称")
    @TableField("DEV_NAME")
    private String devName;

    @ApiModelProperty(value = "分区的盘符路径")
    @TableField("DIR_NAME")
    private String dirName;

    @ApiModelProperty(value = "磁盘类型名，比如本地硬盘、光驱、网络文件系统等")
    @TableField("TYPE_NAME")
    private String typeName;

    @ApiModelProperty(value = "磁盘类型，比如 FAT32、NTFS")
    @TableField("SYS_TYPE_NAME")
    private String sysTypeName;

    @ApiModelProperty(value = "磁盘总大小（单位：byte）")
    @TableField("TOTAL")
    private Long total;

    @ApiModelProperty(value = "磁盘剩余大小（单位：byte）")
    @TableField("FREE")
    private Long free;

    @ApiModelProperty(value = "磁盘已用大小（单位：byte）")
    @TableField("USED")
    private Long used;

    @ApiModelProperty(value = "磁盘可用大小（单位：byte）")
    @TableField("AVAIL")
    private Long avail;

    @ApiModelProperty(value = "磁盘资源的利用率")
    @TableField("USE_PERCENT")
    private Double usePercent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerDiskHistoryVo转MonitorServerDiskHistory
     * </p>
     *
     * @return {@link MonitorServerDiskHistory}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerDiskHistory convertTo() {
        MonitorServerDiskHistory monitorServerDiskHistory = MonitorServerDiskHistory.builder().build();
        BeanUtils.copyProperties(this, monitorServerDiskHistory);
        return monitorServerDiskHistory;
    }

    /**
     * <p>
     * MonitorServerDiskHistory转MonitorServerDiskHistoryVo
     * </p>
     *
     * @param monitorServerDiskHistory {@link MonitorServerDiskHistory}
     * @return {@link MonitorServerDiskHistoryVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerDiskHistoryVo convertFor(MonitorServerDiskHistory monitorServerDiskHistory) {
        BeanUtils.copyProperties(monitorServerDiskHistory, this);
        return this;
    }

}
