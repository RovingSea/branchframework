package org.branchframework.rpc.core.protocol.serialization;

/**
 * 用于扩张序列化、反序列化算法
 * @author Haixin Wu
 * @since 1.0
 */
public abstract class Serializer {

    // 序列化算法
    public abstract  <T> byte[] serialize(T object);

    // 反序列化算法
    public abstract  <T> T deserialize(Class<T> clazz, byte[] bytes);

}

