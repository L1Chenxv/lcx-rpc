package com.lcx.rpc.handler;

import com.lcx.rpc.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author： lichenxu
 * @date： 2024/8/2817:47
 * @description： 空闲检测
 * @version： v1.0
 */
public class RpcIdleStateHandler extends IdleStateHandler {

    public RpcIdleStateHandler() {
        this(25, 0, 0);
    }

    public RpcIdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        this(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds, TimeUnit.SECONDS);
    }

    public RpcIdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds, TimeUnit unit) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds, unit);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println("channelIdle, close channel");
        ctx.channel().close();
    }
}
