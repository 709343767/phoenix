package com.gitee.pifeng.monitoring.common.init;

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

    static {
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
            Method getMonitoringProps = configLoader.getMethod("getMonitoringProperties");
            Object monitoringProps = getMonitoringProps.invoke(null);
            if (monitoringProps == null) {
                log.error("MonitoringProperties 对象为空，无法加载 MonitoringSecureProperties 实例！");
                return null;
            }
            Method getSecure = monitoringProps.getClass().getMethod("getSecure");
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
            Method getAlgorithmType = secureProperties.getClass().getMethod("getEncryptionAlgorithmType");
            Object algoEnum = getAlgorithmType.invoke(secureProperties);
            if (algoEnum == null) {
                return null;
            }
            // 枚举的 name() 方法（注意：不是 getName()）
            Method nameMethod = algoEnum.getClass().getMethod("name");
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
            Method getter = secureProperties.getClass().getMethod(getterName);
            Object configObj = getter.invoke(secureProperties);
            if (configObj == null) {
                return null;
            }
            Method getKey = configObj.getClass().getMethod("getKey");
            return (String) getKey.invoke(configObj);
        } catch (Exception e) {
            log.error("通过 {} 获取加密密钥失败！", getterName, e);
            return null;
        }
    }

}
