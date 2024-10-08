package com.lcx.rpc.handler;

import com.lcx.rpc.common.RpcPromise;
import com.lcx.rpc.common.RpcRequestHolder;
import com.lcx.rpc.common.RpcResponse;
import com.lcx.rpc.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;

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
        RpcPromise<RpcResponse> future = RpcRequestHolder.REQUEST_MAP.remove(requestId);
        if (Objects.nonNull(future)) {
            future.getPromise().setSuccess(msg.getBody());
        }
    }
}
