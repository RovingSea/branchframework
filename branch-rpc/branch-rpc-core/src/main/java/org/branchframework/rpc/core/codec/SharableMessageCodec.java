package org.branchframework.rpc.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.branchframework.rpc.core.context.BranchRpcClientContext;
import org.branchframework.rpc.core.context.BranchRpcServerContext;
import org.branchframework.rpc.core.protocol.ProtocolConstants;
import org.branchframework.rpc.core.protocol.message.Message;
import org.branchframework.rpc.core.protocol.serialization.SerializationAlgorithm;

import java.util.List;

/**
 * <b>
 * +-------------------+<br>
 * | 魔数 ------ 4bytes |<br>
 * | 协议版本 - 1bytes |<br>
 * | 序列化号 - 1bytes | <br>
 * | 请求类型 - 1bytes |<br>
 * | 请求序号 - 4bytes |<br>
 * | 数据长度 - 4bytes |<br>
 * | 对齐填充 - 1bytes |<br>
 * +-------------------+<br>
 * | 数据内容 - ?bytes |<br>
 * +-------------------+<br>
 * </b>
 *
 * @author Haixin Wu
 * @since 1.0
 */
@ChannelHandler.Sharable
public class SharableMessageCodec extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 设置魔数
        out.writeBytes(ProtocolConstants.MagicNum);
        // 设置协议版本号
        out.writeByte(ProtocolConstants.Version);
        // 设置序列化方式
        out.writeByte(BranchRpcClientContext.getSerializationAlgorithm().ordinal());
        // 指令类型，请求或响应
        out.writeByte(message.getMessageType());
        // 请求序号，为了双工通信，提供异步能力
        out.writeInt(message.getSequenceId());
        // 无意义，对齐填充
        out.writeByte(0xff);

        byte[] bytes = BranchRpcClientContext.getSerializer().serialize(message);
        // 设置正文长度
        out.writeInt(bytes.length);
        // 设置消息正文
        out.writeBytes(bytes);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outList) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializationAlgorithmOrdinal = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        Class<? extends Message> messageClass = Message.getMessageClass(messageType);
        Message message = BranchRpcServerContext.getSerializer(serializationAlgorithmOrdinal).deserialize(messageClass, bytes);
        outList.add(message);
    }
}

