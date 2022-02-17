package org.branchframework.rpc.core.protocol.serialization;

/**
 * 序列化的上下文，它关联了序列化器，用于引用具体的序列化算法
 * @author Haixin Wu
 * @since 1.0
 */
public class SerializationContext {

    // TODO 维护一个抽象策略类的引用
    private Serializer serializer;

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    // 序列化算法
    public <T> byte[] serialize(T object) {
        return serializer.serialize(object);
    }

    // 反序列化算法
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return serializer.deserialize(clazz, bytes);
    }

}

