package com.transfar.common.domain.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

import com.transfar.common.abs.SuperBean;

/**
 * <p>
 * 磁盘信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/8 20:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class DiskDomain extends SuperBean {

    /**
     * 磁盘数量
     */
    private int diskNum;

    /**
     * 磁盘信息
     */
    private List<DiskInfoDomain> diskInfoList;

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class DiskInfoDomain extends SuperBean {

        /**
         * 分区的盘符名称
         */
        String devName;

        /**
         * 分区的盘符路径
         */
        String dirName;

        /**
         * 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
         */
        String typeName;

        /**
         * 文件系统类型，比如 FAT32、NTFS
         */
        String sysTypeName;

        /**
         * 文件系统总大小
         */
        String total;

        /**
         * 文件系统剩余大小
         */
        String free;

        /**
         * 文件系统已使用大小
         */
        String used;

        /**
         * 文件系统可用大小
         */
        String avail;

        /**
         * 文件系统资源的利用率
         */
        String usePercent;

        @Override
        public String toString() {
            return "DiskInfoDomain [磁盘名称=" + devName + ", 磁盘路径=" + dirName + ", 磁盘类型名=" + typeName
                    + ", 文件系统=" + sysTypeName + ", 磁盘总大小=" + total + ", 磁盘剩余大小=" + free + ", 磁盘已用大小=" + used
                    + ", 磁盘可用大小=" + avail + ", 磁盘使用率=" + usePercent + "]";
        }

    }

}
