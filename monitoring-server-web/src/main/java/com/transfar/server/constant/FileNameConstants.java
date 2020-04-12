package com.transfar.server.constant;

import java.io.File;

/**
 * <p>
 * 文件名字常量
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月31日 下午4:02:57
 */
public final class FileNameConstants {

    /**
     * 文件存储路径
     */
    private final static String PATH = System.getProperty("user.dir")
            + File.separator + "persistent-monitoring" + File.separator + "ini" + File.separator;

    /**
     * 应用实例池
     */
    public final static String INSTANCE_POOL = PATH + "instancePool.json";

    /**
     * 网络信息池
     */
    public final static String NET_POOL = PATH + "netPool.json";

    /**
     * 服务器内存池
     */
    public final static String MEMORY_POOL = PATH + "memoryPool.json";

    /**
     * 服务器CPU池
     */
    public final static String CPU_POOL = PATH + "cpuPool.json";

    /**
     * 服务器磁盘池
     */
    public final static String DISK_POOL = PATH + "diskPool.json";

}
