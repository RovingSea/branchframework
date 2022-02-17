package org.branchframework.rpc.client.processor;

import lombok.extern.slf4j.Slf4j;
import org.branchframework.rpc.client.annotation.BranchRpcReference;
import org.branchframework.rpc.client.transmission.NettyRpcClientManager;
import org.branchframework.rpc.core.context.BranchRpcClientContext;
import org.branchframework.rpc.core.discovery.DiscoveryService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Slf4j
public class RpcClientProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

    private static DiscoveryService discoveryService;

    private final NettyRpcClientManager nettyRpcClientManager;

    private ApplicationContext applicationContext;

    public RpcClientProcessor(NettyRpcClientManager nettyRpcClientManager) {
        this.nettyRpcClientManager = nettyRpcClientManager;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.getClass().getClassLoader());
                ReflectionUtils.doWithFields(clazz, field -> {
                    BranchRpcReference branchRpcReference = AnnotationUtils.getAnnotation(field, BranchRpcReference.class);
                    if (branchRpcReference != null) {
                        try {
                            discoveryService = BranchRpcClientContext.getRegistry();
                            String version = branchRpcReference.version();
                            Object bean = applicationContext.getBean(clazz);
                            field.setAccessible(true);
                            // 修改为代理对象
                            ReflectionUtils.setField(field, bean, nettyRpcClientManager.setTarget(field.getType(), version, discoveryService));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

