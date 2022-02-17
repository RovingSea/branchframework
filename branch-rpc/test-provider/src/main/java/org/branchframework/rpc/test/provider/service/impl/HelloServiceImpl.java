package org.branchframework.rpc.test.provider.service.impl;

import org.branchframework.rpc.server.annotation.BranchRpcService;
import org.branchframework.rpc.test.service.HelloService;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@BranchRpcService(interfaceType = HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "您好，" + name;
    }
}

