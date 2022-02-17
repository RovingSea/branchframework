package org.branchframework.rpc.core.protocol.message;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Data
public abstract class Message implements Serializable {

    protected int sequenceId;

    protected int messageType;

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    public static final int RpcRequestMessageOrdinal = 1;
    public static final int RpcResponseMessageOrdinal = 0;

    static {
        messageClasses.put(RpcRequestMessageOrdinal, RpcRequestMessage.class);
    }

    /**
     * 根据消息字节类型，获取对应的消息 class
     * @param messageType 消息类型
     * @return 消息对应的 class
     */
    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    public abstract int getMessageType();
}

