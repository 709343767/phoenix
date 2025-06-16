package com.gitee.pifeng.monitoring.common.util.server.oshi;

import com.gitee.pifeng.monitoring.common.domain.server.GpuDomain;
import com.gitee.pifeng.monitoring.common.init.InitOshi;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import oshi.hardware.GraphicsCard;

import java.util.List;

/**
 * <p>
 * GPU工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/6/6 11:35
 */
@Slf4j
public class GpuUtils extends InitOshi {

    /**
     * <p>
     * 获取Gpu信息
     * </p>
     *
     * @return {@link GpuDomain}
     * @author 皮锋
     * @custom.date 2025/6/6 16:49
     */
    public static GpuDomain getGpuInfo() {
        try {
            List<GraphicsCard> graphicsCards = SYSTEM_INFO.getHardware().getGraphicsCards();
            // 创建返回对象
            GpuDomain gpuDomain = new GpuDomain();
            List<GpuDomain.GpuInfoDomain> gpuInfoDomains = Lists.newArrayList();
            for (GraphicsCard card : graphicsCards) {
                GpuDomain.GpuInfoDomain gpuInfoDomain = new GpuDomain.GpuInfoDomain();
                gpuInfoDomain.setGpuName(card.getName());
                gpuInfoDomain.setGpuDeviceId(card.getDeviceId());
                gpuInfoDomain.setGpuVendor(card.getVendor());
                gpuInfoDomain.setGpuVersionInfo(card.getVersionInfo());
                gpuInfoDomain.setGpuVRamTotal(card.getVRam());
                gpuInfoDomains.add(gpuInfoDomain);
            }
            gpuDomain.setGpuNum(graphicsCards.size()).setGpuList(gpuInfoDomains);
            return gpuDomain;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}