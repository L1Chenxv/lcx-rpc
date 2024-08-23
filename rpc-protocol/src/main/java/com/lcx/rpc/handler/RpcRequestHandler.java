package com.lcx.rpc.handler;

import com.lcx.rpc.common.RpcRequest;
import com.lcx.rpc.common.RpcResponse;
import com.lcx.rpc.protocol.RpcProtocol;
import com.lcx.rpc.protocol.MsgHeader;
import com.lcx.rpc.protocol.MsgStatus;
import com.lcx.rpc.protocol.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author： lichenxu
 * @date： 2024/8/2314:13
 * @description： 请求处理 -> 服务提供者
 * @version： v1.0
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private final Map<String, Object> rpcServiceMap;

    public RpcRequestHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception {
        RpcRequestProcessor.submitRequest(() -> {
            RpcProtocol<Object> resProtocol = new RpcProtocol<>();
            RpcResponse response = new RpcResponse();
            MsgHeader header = protocol.getHeader();
            header.setMsgType((byte) MsgType.RESPONSE.getType());
            try {
                // 获取处理结果
                Object result = handle(protocol.getBody());
                response.setData(result);
                header.setStatus((byte) MsgStatus.SUCCESS.getCode());
                // 设置协议体
                resProtocol.setHeader(header);
                resProtocol.setBody(response);
            } catch (Throwable throwable) {
                header.setStatus((byte) MsgStatus.FAIL.getCode());
                response.setMessage(throwable.toString());
                log.error("process request {} error", header.getRequestId(), throwable);
            }
            ctx.writeAndFlush(resProtocol);
        });
    }

    private Object handle(RpcRequest request) {
        // TODO
        return null;
    }
}
