package org.branchframework.rpc.server.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface BranchRpcService {

    /**
     * 暴露服务接口类型
     */
    Class<?> interfaceType() default Object.class;

    /**
     * 服务版本
     */
    String version() default "1.0";

}
