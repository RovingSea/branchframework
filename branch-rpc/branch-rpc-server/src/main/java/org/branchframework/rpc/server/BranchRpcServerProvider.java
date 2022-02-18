package org.branchframework.rpc.server;

import lombok.extern.slf4j.Slf4j;
import org.branchframework.rpc.core.cache.LocalServerCache;
import org.branchframework.rpc.core.registry.RegistryServiceNode;
import org.branchframework.rpc.core.registry.RegistryServiceNodeOperation;
import org.branchframework.rpc.core.registry.util.RegistryServiceUtil;
import org.branchframework.rpc.server.annotation.BranchRpcService;
import org.branchframework.rpc.server.config.RpcServerProperties;
import org.branchframework.rpc.server.transmission.StartRpcServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;

import java.net.InetAddress;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Slf4j
public class BranchRpcServerProvider implements BeanPostProcessor, CommandLineRunner {

    private final RpcServerProperties properties;
    private final RegistryServiceNodeOperation registryServiceNodeOperation;
    private final StartRpcServer rpcServer;

    @Autowired
    public BranchRpcServerProvider(RpcServerProperties properties, RegistryServiceNodeOperation registryServiceNodeOperation, StartRpcServer rpcServer) {
        this.properties = properties;
        this.registryServiceNodeOperation = registryServiceNodeOperation;
        this.rpcServer = rpcServer;
    }


    /**
     * 使所有加了 @BranchRpcServer 注解的类<br>
     * 暴露服务到注册中心<br>
     * 容器启动后开启 netty 服务处理请求
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        BranchRpcService branchRpcService = bean.getClass().getAnnotation(BranchRpcService.class);
        if (branchRpcService != null) {
            try {
                String serviceName = branchRpcService.interfaceType().getName();
                String version = branchRpcService.version();
                String nameAndVersion = RegistryServiceUtil.bindNameAndVersion(serviceName, version);
                LocalServerCache.put(nameAndVersion, bean);

                RegistryServiceNode serviceNode = new RegistryServiceNode();
                serviceNode.setServiceName(nameAndVersion);
                serviceNode.setAddress(InetAddress.getLocalHost().getHostAddress());
                serviceNode.setPort(properties.getPort());
                serviceNode.setAppName(properties.getAppName());
                serviceNode.setVersion(version);

                registryServiceNodeOperation.register(serviceNode);
            } catch (Exception e) {
                log.error("在服务启动时的注册环节出错：{}", e.getMessage());
            }
        }
        return bean;
    }

    /**
     * 启动 rpc 服务
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> rpcServer.start(properties.getPort())).start();
        log.info("有关Branch框架的RPC服务端已启动");
        log.info("服务对象：{}", rpcServer);
        log.info("服务名：{}", properties.getAppName());
        log.info("端口号：{}", properties.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                registryServiceNodeOperation.destroy();
                log.info("关闭时已安全清除服务节点");
            } catch (Exception e) {
                log.error("关闭时清除节点发送了异常：{}", e.getMessage());
            }
        }));
    }


}

