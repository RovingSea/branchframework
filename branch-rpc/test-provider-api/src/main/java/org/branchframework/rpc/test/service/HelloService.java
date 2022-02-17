package org.branchframework.rpc.test.service;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public interface HelloService {
    String sayHello(String name) throws InterruptedException;
    //如果要使用异步方式，返回类型要注明为 Object
    Object sayHello1(String name) throws InterruptedException;
}