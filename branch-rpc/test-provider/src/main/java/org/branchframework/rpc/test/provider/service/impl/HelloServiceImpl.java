package org.branchframework.rpc.test.provider.service.impl;

import org.branchframework.rpc.server.annotation.BranchRpcService;
import org.branchframework.rpc.test.service.HelloService;


import java.util.concurrent.TimeUnit;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@BranchRpcService(interfaceType = HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return "您好，" + name;
    }

    @Override
    public Object sayHello1(String name) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return "您好，" + name;
    }
}

