package com.lcx.rpc.codec;

import com.lcx.rpc.common.HeartBeatData;
import com.lcx.rpc.common.RpcRequest;
import com.lcx.rpc.common.RpcResponse;
import com.lcx.rpc.protocol.RpcProtocol;
import com.lcx.rpc.protocol.MsgHeader;
import com.lcx.rpc.protocol.MsgType;
import com.lcx.rpc.protocol.ProtocolConstants;
import com.lcx.rpc.serialization.RpcSerialization;
import com.lcx.rpc.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author： lichenxu
 * @date： 2024/8/2311:11
 * @description： 解码
 * @version： v1.0
 */
public class RpcDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < ProtocolConstants.HEADER_TOTAL_LEN) {
            return;
        }
        in.markReaderIndex();
        short magic = in.readShort();
        if (magic != ProtocolConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }
        byte version = in.readByte();
        byte serializeType = in.readByte();
        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();
        int dataLength = in.readInt();

        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        MsgType msgTypeEnum = MsgType.findByType(msgType);
        if (msgTypeEnum == null) {
            return;
        }

        MsgHeader header = new MsgHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serializeType);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setMsgType(msgType);
        header.setMsgLen(dataLength);

        RpcSerialization rpcSerialization = SerializationFactory.getRpcSerialization(serializeType);
        switch (msgTypeEnum) {
            case REQUEST:
                RpcRequest request = rpcSerialization.deserialize(data, RpcRequest.class);
                if (request != null) {
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    out.add(protocol);
                }
                break;
            case RESPONSE:
                RpcResponse response = rpcSerialization.deserialize(data, RpcResponse.class);
                if (response != null) {
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    out.add(protocol);
                }
                break;
            case HEARTBEAT:
                HeartBeatData heartbeat = rpcSerialization.deserialize(data, HeartBeatData.class);
                if (heartbeat != null) {
                    RpcProtocol<HeartBeatData> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(heartbeat);
                    out.add(protocol);
                }
                break;
            default:
                throw new IllegalArgumentException("message type is is illegal," + msgTypeEnum);
        }
    }
}
