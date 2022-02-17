package org.branchframework.rpc.core.context;

import org.branchframework.rpc.core.protocol.serialization.GsonSerializer;
import org.branchframework.rpc.core.protocol.serialization.JdkSerializer;
import org.branchframework.rpc.core.protocol.serialization.SerializationAlgorithm;
import org.branchframework.rpc.core.protocol.serialization.Serializer;

import java.io.IOException;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public class BranchRpcServerContext {
    public static Serializer getSerializer(int SerializationAlgorithmOrdinal) throws IOException {
        if (SerializationAlgorithmOrdinal == SerializationAlgorithm.Java.ordinal()) {
            return new JdkSerializer();
        } else if (SerializationAlgorithmOrdinal == SerializationAlgorithm.Gson.ordinal()) {
            return new GsonSerializer();
        }
        throw new IOException("没有找到匹配的序列化算法");
    }
}

