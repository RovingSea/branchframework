package org.branchframework.rpc.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "branch.rpc.server")
public class RpcServerProperties {
    /**
     * 服务启动的端口
     */
    private Integer port;
    /**
     * 服务名称
     */
    private String appName;
    /**
     * 注册中心地址
     */
    private String registryAddress;
}

