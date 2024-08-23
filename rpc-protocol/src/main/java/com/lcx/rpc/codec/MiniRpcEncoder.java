package com.lcx.rpc.codec;

import com.lcx.rpc.common.MiniRpcRequest;
import com.lcx.rpc.protocol.MiniRpcProtocol;
import com.lcx.rpc.protocol.MsgHeader;
import com.lcx.rpc.serialization.RpcSerialization;
import com.lcx.rpc.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author： lichenxu
 * @date： 2024/8/2311:12
 * @description： 编码
 * @version： v1.0
 */
public class MiniRpcEncoder extends MessageToByteEncoder<MiniRpcProtocol<Object>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MiniRpcProtocol<Object> msg, ByteBuf byteBuf) throws Exception {
        MsgHeader header = msg.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getSerialization());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());

        RpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(header.getSerialization());
        byte[] data = rpcSerialization.serialize(msg.getBody());
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);

    }
}
