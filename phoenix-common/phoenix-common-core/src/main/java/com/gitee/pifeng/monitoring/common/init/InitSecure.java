package com.gitee.pifeng.monitoring.common.init;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.gitee.pifeng.monitoring.common.constant.SecurerEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * <p>
 * 初始化加解密配置
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/11/22 12:18
 */
@Slf4j
public class InitSecure {

    /**
     * 加解密类型
     */
    public static final String SECRET_TYPE;

    /**
     * DES密钥
     */
    public static final String SECRET_KEY_DES;

    /**
     * AES密钥
     */
    public static final String SECRET_KEY_AES;

    /**
     * 国密SM4密钥
     */
    public static final String SECRET_KEY_SM4;

    /*
     *<p>
     *初始化加解密配置
     *</p>
     *
     *@author 皮锋
     * @custom.date 2025/12/24 19:16
     */
    static {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        try {
            // 通过反射加载 MonitoringSecureProperties 实例
            Object secureProps = loadSecureProperties();
            if (secureProps != null) {
                SECRET_TYPE = getSecretType(secureProps);
                if (StringUtils.equalsIgnoreCase(SecurerEnums.DES.name(), SECRET_TYPE)) {
                    SECRET_KEY_DES = getSecretKey(secureProps, "getDes");
                } else {
                    SECRET_KEY_DES = null;
                }
                if (StringUtils.equalsIgnoreCase(SecurerEnums.AES.name(), SECRET_TYPE)) {
                    SECRET_KEY_AES = getSecretKey(secureProps, "getAes");
                } else {
                    SECRET_KEY_AES = null;
                }
                if (StringUtils.equalsIgnoreCase(SecurerEnums.SM4.name(), SECRET_TYPE)) {
                    SECRET_KEY_SM4 = getSecretKey(secureProps, "getSm4");
                } else {
                    SECRET_KEY_SM4 = null;
                }
                log.info("初始化加解密配置成功！加解密类型：{}", StringUtils.defaultIfBlank(SECRET_TYPE, "无"));
            } else {
                // 如果加载失败，设为 null
                SECRET_TYPE = null;
                SECRET_KEY_DES = null;
                SECRET_KEY_AES = null;
                SECRET_KEY_SM4 = null;
                log.warn("未加载到 MonitoringSecureProperties 配置，加解密功能将被禁用！");
            }
        } finally {
            // 时间差（毫秒）
            String betweenDay = timer.intervalPretty();
            log.info("初始化加解密配置耗时：{}", betweenDay);
        }
    }

    /**
     * <p>
     * 通过反射加载 MonitoringSecureProperties 实例
     * </p>
     *
     * @return MonitoringSecureProperties 实例，若加载失败则返回 null
     * @author 皮锋
     * @custom.date 2025年12月7日 17:19:50
     */
    private static Object loadSecureProperties() {
        try {
            Class<?> configLoader = Class.forName("com.gitee.pifeng.monitoring.plug.core.ConfigLoader");
            Method getMonitoringProperties = configLoader.getMethod("getMonitoringProperties");
            // 关闭权限检查，提升 invoke 性能
            setAccessibleQuietly(getMonitoringProperties);
            Object monitoringProps = getMonitoringProperties.invoke(null);
            if (monitoringProps == null) {
                log.error("MonitoringProperties 对象为空，无法加载 MonitoringSecureProperties 实例！");
                return null;
            }
            Method getSecure = monitoringProps.getClass().getMethod("getSecure");
            // 关闭权限检查，提升 invoke 性能
            setAccessibleQuietly(getSecure);
            return getSecure.invoke(monitoringProps);
        } catch (ClassNotFoundException e) {
            log.error("未找到 ConfigLoader 类！");
            return null;
        } catch (Exception e) {
            log.error("通过反射加载 MonitoringSecureProperties 实例时发生异常！", e);
            return null;
        }
    }

    /**
     * <p>
     * 从 MonitoringSecureProperties 对象中获取加密算法类型的枚举名称
     * </p>
     *
     * @param secureProperties MonitoringSecureProperties 实例
     * @return 枚举名称字符串（如 "AES"），若获取失败则返回 null
     * @author 皮锋
     * @custom.date 2025年12月7日 17:19:50
     */
    private static String getSecretType(Object secureProperties) {
        try {
            Method getEncryptionAlgorithmType = secureProperties.getClass().getMethod("getEncryptionAlgorithmType");
            // 关闭权限检查，提升 invoke 性能
            setAccessibleQuietly(getEncryptionAlgorithmType);
            Object algoEnum = getEncryptionAlgorithmType.invoke(secureProperties);
            if (algoEnum == null) {
                return null;
            }
            // 枚举的 name() 方法（注意：不是 getName()）
            Method nameMethod = algoEnum.getClass().getMethod("name");
            // 关闭权限检查，提升 invoke 性能
            setAccessibleQuietly(nameMethod);
            return (String) nameMethod.invoke(algoEnum);
        } catch (Exception e) {
            log.error("获取加密算法类型失败！", e);
            return null;
        }
    }

    /**
     * <p>
     * 从 MonitoringSecureProperties 对象中获取指定加密配置的密钥
     * </p>
     *
     * @param secureProperties MonitoringSecureProperties 实例
     * @param getterName       获取配置对象的方法名，如 "getDes"、"getAes"、"getSm4"
     * @return 密钥字符串，若获取失败则返回 null
     * @author 皮锋
     * @custom.date 2025年12月7日 17:19:50
     */
    private static String getSecretKey(Object secureProperties, String getterName) {
        try {
            Method method = secureProperties.getClass().getMethod(getterName);
            // 关闭权限检查，提升 invoke 性能
            setAccessibleQuietly(method);
            Object configObj = method.invoke(secureProperties);
            if (configObj == null) {
                return null;
            }
            Method getKey = configObj.getClass().getMethod("getKey");
            // 关闭权限检查，提升 invoke 性能
            setAccessibleQuietly(getKey);
            return (String) getKey.invoke(configObj);
        } catch (Exception e) {
            log.error("通过 {} 获取加密密钥失败！", getterName, e);
            return null;
        }
    }

    /**
     * <p>
     * 安静地设置 {@link Method} 对象为可访问（accessible），用于提升反射调用性能或绕过访问控制限制。<br>
     * 该方法会尝试调用 {@link Method#setAccessible(boolean)} 并忽略可能抛出的 {@link SecurityException}，
     * 避免在安全管理器 {@link SecurityManager} 启用或受限环境中导致程序异常中断。
     * </p>
     *
     * @param method method 要设置为可访问的 {@link Method} 对象，不可为 {@code null}
     * @author 皮锋
     * @custom.date 2025/12/24 19:23
     */
    private static void setAccessibleQuietly(Method method) {
        try {
            method.setAccessible(true);
        } catch (SecurityException e) {
            log.trace("无法设置方法 {} 为可访问（accessible），继续使用默认访问控制！", method.getName(), e);
        }
    }

    /**
     * <p>
     * 主动触发本类的静态初始化块，用于提前加载加解密配置，避免在首次加解密操作时因类加载和反射导致的延迟。
     * 此方法无参数、无返回值、无副作用，可安全多次调用。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2025/12/25 10:21
     */
    public static void declare() {
        // 仅用于触发 static 块执行，无需任何逻辑
        log.trace("触发 InitSecure 静态初始化（若尚未执行）！");
    }

}
