package com.lcx.rpc.handler;

import com.lcx.rpc.common.RpcRequest;
import com.lcx.rpc.common.RpcResponse;
import com.lcx.rpc.common.RpcServiceHelper;
import com.lcx.rpc.protocol.MsgHeader;
import com.lcx.rpc.protocol.MsgStatus;
import com.lcx.rpc.protocol.MsgType;
import com.lcx.rpc.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author： lichenxu
 * @date： 2024/8/2314:13
 * @description： 请求处理 -> 服务提供者
 * @version： v1.0
 */
@Slf4j
public class RpcRequestHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Object> rpcServiceMap;

    public RpcRequestHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequestProcessor.submitRequest(() -> {
            RpcProtocol<Object> resProtocol = new RpcProtocol<>();
            RpcResponse response = new RpcResponse();
            RpcProtocol protocol = (RpcProtocol) msg;
            MsgHeader header = protocol.getHeader();
            header.setMsgType((byte) MsgType.RESPONSE.getType());

            try {
                if (protocol.getBody() instanceof RpcRequest) {
                    // 获取处理结果
                    Object result = handle((RpcRequest) (protocol.getBody()));
                    response.setData(result);
                } else {
                    response.setMessage("heartbeat success");
                    System.out.println("heartbeat success，客户端仍旧存活");
                }

                header.setStatus((byte) MsgStatus.SUCCESS.getCode());
                // 设置协议体
                resProtocol.setHeader(header);
                resProtocol.setBody(response);

            } catch (Throwable throwable) {
                header.setStatus((byte) MsgStatus.FAIL.getCode());
                response.setMessage(throwable.toString());
                log.error("process request {} error", header.getRequestId(), throwable);
            }

            ctx.pipeline().writeAndFlush(resProtocol);
        });


    }

    private Object handle(RpcRequest request) throws InvocationTargetException {
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        Object serviceBean = rpcServiceMap.get(serviceKey);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] params = request.getParams();

        FastClass fastClass = FastClass.create(serviceClass);
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);

        return fastClass.invoke(methodIndex, serviceBean, params);
    }


}
