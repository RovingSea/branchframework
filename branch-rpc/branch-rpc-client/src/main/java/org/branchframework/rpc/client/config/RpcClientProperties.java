package org.branchframework.rpc.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "branch.rpc.client")
public class RpcClientProperties {
    /**
     * 注册中心
     */
    private String registry;
    /**
     * 序列化
     */
    private String serialization;
    /**
     * 注册中心的地址
     */
    private String registryAddress;
}

