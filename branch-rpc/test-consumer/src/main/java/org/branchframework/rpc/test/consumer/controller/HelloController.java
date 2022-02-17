package org.branchframework.rpc.test.consumer.controller;

import io.netty.util.concurrent.Future;
import org.branchframework.rpc.client.annotation.BranchRpcReference;
import org.branchframework.rpc.test.service.HelloService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@RestController
@RequestMapping("/test")
@CrossOrigin
public class HelloController {

    @BranchRpcReference(version = "1.0", sync = true)
    private HelloService helloService;

    @BranchRpcReference(version = "1.0", sync = false)
    private HelloService helloService1;

    @GetMapping("/sync/hello/{name}")
    public String hello(@PathVariable String name) throws InterruptedException {
        return helloService.sayHello(name);
    }

    @GetMapping("/async/hello/{name}/{waitTime}")
    public String hello1(@PathVariable String name, @PathVariable Integer waitTime) throws InterruptedException {
        Future<String> future = (Future<String>) helloService1.sayHello1(name);
        TimeUnit.MILLISECONDS.sleep(waitTime);
        return future.getNow();
    }

}

