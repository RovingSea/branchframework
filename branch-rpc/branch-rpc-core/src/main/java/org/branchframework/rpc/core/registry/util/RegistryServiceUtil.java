package org.branchframework.rpc.core.registry.util;

/**
 * 注册服务时的工具类
 * @author Haixin Wu
 * @since 1.0
 */
public class RegistryServiceUtil {
    /**
     * 将服务名和版本合并统一，按 - 隔开
     * @param serviceName 服务名
     * @param version 版本
     * @return 服务名 - 版本
     */
    public static String bindNameAndVersion(String serviceName, String version) {
        return String.join("-", serviceName, version);
    }
}

