package com.lcx.rpc.handler;

import com.lcx.rpc.common.HeartBeatData;
import com.lcx.rpc.protocol.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @author： lichenxu
 * @date： 2024/8/2818:10
 * @description：
 * @version： v1.0
 */
public class RpcHeartBeatHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);

        // 必须放在扩展操作点之后
        doHeartBeatTask(ctx);

    }

    public void doHeartBeatTask(ChannelHandlerContext ctx) {


        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                RpcProtocol<HeartBeatData> rpcProtocol = buildHeartBeatData();
                ctx.writeAndFlush(rpcProtocol).addListener(future -> {
                    if (!future.isSuccess()) {
                        // 处理发送失败的情况
                        System.err.println("Failed to send heartbeat: " + future.cause());
                    }
                });
                doHeartBeatTask(ctx);
            } else {
                System.out.println("Channel is not active, skipping heartbeat.");
            }

        }, 10, TimeUnit.SECONDS);
    }


    private RpcProtocol<HeartBeatData> buildHeartBeatData() {
        HeartBeatData heartBeatData = new HeartBeatData();

        RpcProtocol<HeartBeatData> protocol = RpcProtocol.buildProtocol(MsgType.HEARTBEAT, heartBeatData);

        return protocol;
    }
}
