package org.branchframework.rpc.client.config;

import org.branchframework.rpc.client.processor.RpcClientProcessor;
import org.branchframework.rpc.client.transmission.NettyRpcClientManager;
import org.branchframework.rpc.core.balancer.LoadBalance;
import org.branchframework.rpc.core.balancer.RoundRobinLoadBalance;
import org.branchframework.rpc.core.discovery.DiscoveryService;
import org.branchframework.rpc.core.discovery.ZkDiscoveryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Configuration
public class RpcClientAutoConfiguration {

    @Bean
    public RpcClientProperties rpcClientProperties(Environment environment) {
        BindResult<RpcClientProperties> result = Binder.get(environment).bind("branch.rpc.client", RpcClientProperties.class);
        return result.get();
    }

    @Bean
    @ConditionalOnMissingBean
    public NettyRpcClientManager rpcClientManager() {
        return new NettyRpcClientManager();
    }

    @Primary
    @Bean(name = "roundRobinLoadBalance")
    @ConditionalOnMissingBean
    public LoadBalance roundRobinLoadBalance() {
        return new RoundRobinLoadBalance();
    }

    @Primary
    @Bean(name = "randomLoadBalance")
    @ConditionalOnMissingBean
    public LoadBalance randomLoadBalance() {
        return new RoundRobinLoadBalance();
    }

//    TODO 更多负载均衡算法
//    @Primary
//    @Bean(name = "xxxLoadBalance")
//    @ConditionalOnMissingBean
//    public LoadBalance xxxLoadBalance() {
//        return new xxxLoadBalance();
//    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({RpcClientProperties.class, LoadBalance.class})
    public DiscoveryService discoveryService(@Autowired RpcClientProperties properties) {
        return new ZkDiscoveryServiceImpl(properties.getRegistryAddress());
    }

//    TODO 更多的发现注册中心服务实现类
//    @Bean
//    @ConditionalOnMissingBean
//    public DiscoveryService discoveryService(@Autowired LoadBalance loadBalance) {
//        return new xxxxxxDiscoveryServiceImpl(properties.getDiscoveryAddress(), loadBalance);
//    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({NettyRpcClientManager.class})
    public RpcClientProcessor rpcClientProvider(@Autowired NettyRpcClientManager nettyRpcClientManager) {
        return new RpcClientProcessor(nettyRpcClientManager);
    }


}

