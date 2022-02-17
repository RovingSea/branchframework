package org.branchframework.rpc.core.registry;

import lombok.Data;

/**
 * 注册中心的服务节点
 * @author Haixin Wu
 * @since 1.0
 */
@Data
public class RegistryServiceNode {
    /**
     *  应用名称
     */
    private String appName;

    /**
     *  服务名称
     */
    private String serviceName;

    /**
     *  版本
     */
    private String version;

    /**
     *  地址
     */
    private String address;

    /**
     *  端口
     */
    private Integer port;

    /**
     * 权值
     */
    private Integer weight;
}

