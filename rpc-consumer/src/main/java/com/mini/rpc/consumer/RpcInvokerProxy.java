package com.mini.rpc.consumer;

import com.lcx.rpc.common.RpcFuture;
import com.lcx.rpc.common.RpcRequest;
import com.lcx.rpc.common.RpcRequestHolder;
import com.lcx.rpc.common.RpcResponse;
import com.lcx.rpc.protocol.*;
import com.lcx.rpc.provider.registry.RegistryService;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import org.jboss.netty.handler.codec.socks.SocksMessage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author： lichenxu
 * @date： 2024/8/2615:32
 * @description： RPC 动态代理类
 * @version： v1.0
 */
public class RpcInvokerProxy implements InvocationHandler {

    private final String serviceVersion;
    private final long timeout;
    private final RegistryService registryService;

    public RpcInvokerProxy(String serviceVersion, long timeout, RegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构造 RPC 协议对象
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        // 构造 RPC 协议头
        MsgHeader header = new MsgHeader();
        long requestId = RpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(requestId);
        header.setMsgType((byte) MsgType.REQUEST.getType());
        header.setStatus((byte) MsgStatus.FAIL.getCode());
        protocol.setHeader(header);
        // 构造 RPC 协议体
        RpcRequest request = new RpcRequest();
        request.setServiceVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParams(args);
        request.setParameterTypes(method.getParameterTypes());
        protocol.setBody(request);

        RpcConsumer rpcConsumer = new RpcConsumer();
        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);
        // 发起 RPC 远程调用
        rpcConsumer.sendRequest(protocol, registryService);
        // 等待 RPC 调用执行结果
        // TODO hold request by ThreadLocal
        Object result = future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
        return result;
    }
}
