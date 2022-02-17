package org.branchframework.rpc.server.config;

import org.branchframework.rpc.core.registry.RegistryServiceNodeOperation;
import org.branchframework.rpc.core.registry.ZkRegistryServiceNodeOpImpl;
import org.branchframework.rpc.server.BranchRpcServerProvider;
import org.branchframework.rpc.server.transmission.NettyStartRpcServer;
import org.branchframework.rpc.server.transmission.StartRpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcServerAutoConfiguration {

    private final RpcServerProperties properties;

    @Autowired
    public RpcServerAutoConfiguration(RpcServerProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public RegistryServiceNodeOperation registryServiceNodeOperation() {
        return new ZkRegistryServiceNodeOpImpl(properties.getRegistryAddress());
    }

    @Bean
    @ConditionalOnMissingBean(NettyStartRpcServer.class)
    StartRpcServer rpcServer() {
        return new NettyStartRpcServer();
    }

    @Bean
    @ConditionalOnMissingBean(BranchRpcServerProvider.class)
    public BranchRpcServerProvider rpcServerProvider(@Autowired RpcServerProperties properties,
                                                     @Autowired RegistryServiceNodeOperation registryServiceNodeOperation,
                                                     @Autowired StartRpcServer rpcServer) {
        return new BranchRpcServerProvider(properties, registryServiceNodeOperation, rpcServer);
    }

}

