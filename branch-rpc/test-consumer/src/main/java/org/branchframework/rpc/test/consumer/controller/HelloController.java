package org.branchframework.rpc.test.consumer.controller;


import org.branchframework.rpc.client.annotation.BranchRpcReference;
import org.branchframework.rpc.test.service.HelloService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@RestController
@RequestMapping("/test")
@CrossOrigin
public class HelloController {
    @BranchRpcReference(version = "1.0")
    private HelloService helloService;

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return helloService.sayHello(name);
    }

}

