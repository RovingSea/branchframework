package org.branchframework.rpc.test.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@SpringBootApplication
@ComponentScan("org.branchframework.rpc")
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}

