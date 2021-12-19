package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerDisk;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * <p>
 * 服务器磁盘信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/10/22 17:27
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "服务器磁盘信息表现层对象")
public class MonitorServerDiskVo implements ISuperBean {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "磁盘序号")
    private Integer diskNo;

    @ApiModelProperty(value = "分区的盘符名称")
    private String devName;

    @ApiModelProperty(value = "分区的盘符路径")
    private String dirName;

    @ApiModelProperty(value = "磁盘类型名，比如本地硬盘、光驱、网络文件系统等")
    private String typeName;

    @ApiModelProperty(value = "磁盘类型，比如 FAT32、NTFS")
    private String sysTypeName;

    @ApiModelProperty(value = "磁盘总大小（单位：byte）")
    private Long total;

    @ApiModelProperty(value = "磁盘剩余大小（单位：byte）")
    private Long free;

    @ApiModelProperty(value = "磁盘已用大小（单位：byte）")
    private Long used;

    @ApiModelProperty(value = "磁盘可用大小（单位：byte）")
    private Long avail;

    @ApiModelProperty(value = "磁盘资源的利用率")
    private Double usePercent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "新增时间")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * <p>
     * MonitorServerDiskVo转MonitorServerDisk
     * </p>
     *
     * @return {@link MonitorServerDisk}
     * @author 皮锋
     * @custom.date 2020/9/3 9:20
     */
    public MonitorServerDisk convertTo() {
        MonitorServerDisk monitorServerDisk = MonitorServerDisk.builder().build();
        BeanUtils.copyProperties(this, monitorServerDisk);
        return monitorServerDisk;
    }

    /**
     * <p>
     * MonitorServerDisk转MonitorServerDiskVo
     * </p>
     *
     * @param monitorServerDisk {@link MonitorServerDisk}
     * @return {@link MonitorServerDiskVo}
     * @author 皮锋
     * @custom.date 2020/9/3 9:22
     */
    public MonitorServerDiskVo convertFor(MonitorServerDisk monitorServerDisk) {
        if (null != monitorServerDisk) {
            BeanUtils.copyProperties(monitorServerDisk, this);
        }
        return this;
    }

}
