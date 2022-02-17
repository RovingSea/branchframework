package org.branchframework.rpc.core.protocol.message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 序号自增长赋值器，用来确保消息池中的序号唯一
 * @author Haixin Wu
 * @since 1.0
 */
public class SequenceIdGenerator {

    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }
}

