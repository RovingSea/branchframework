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
     * 负载均衡
     */
    private String balance;
    /**
     * 序列化
     */
    private String serialization;
    /**
     * 服务发现的地址
     */
    private String discoveryAddress;
    /**
     * 服务调用最大等待时间
     */
    private Integer timeout;
}

