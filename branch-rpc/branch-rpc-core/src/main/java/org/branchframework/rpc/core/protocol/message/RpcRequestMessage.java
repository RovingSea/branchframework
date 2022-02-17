package org.branchframework.rpc.core.protocol.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class RpcRequestMessage extends Message {

    /**
     * 调用的接口全限定名，服务端根据它找到实现
     */
    private final String interfaceName;
    /**
     * 调用接口中的方法名
     */
    private final String methodName;
    /**
     * 方法返回类型
     */
    private final Class<?> returnType;
    /**
     * 方法参数类型数组
     */
    private final Class[] parameterTypes;
    /**
     * 方法参数值数组
     */
    private final Object[] parameterValue;
    /**
     * 执行方法的版本
     */
    private final String methodVersion;

    public RpcRequestMessage(int sequenceId, String interfaceName, String methodName, Class<?> returnType, Class[] parameterTypes, Object[] parameterValue, String methodVersion) {
        this.sequenceId = sequenceId;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterValue = parameterValue;
        this.methodVersion = methodVersion;
    }

    @Override
    public int getMessageType() {
        return RpcRequestMessageOrdinal;
    }

}

