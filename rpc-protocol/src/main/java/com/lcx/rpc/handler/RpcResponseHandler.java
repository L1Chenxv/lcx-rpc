package com.lcx.rpc.handler;

import com.lcx.rpc.common.RpcFuture;
import com.lcx.rpc.common.RpcRequestHolder;
import com.lcx.rpc.common.RpcResponse;
import com.lcx.rpc.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author： lichenxu
 * @date： 2024/8/2314:58
 * @description：
 * @version： v1.0
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg) throws Exception {
        long requestId = msg.getHeader().getRequestId();
        RpcFuture<RpcResponse> future = RpcRequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(msg.getBody());
    }
}
