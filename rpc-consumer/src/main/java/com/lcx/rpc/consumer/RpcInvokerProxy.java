package com.lcx.rpc.consumer;

import com.lcx.rpc.common.RpcPromise;
import com.lcx.rpc.common.RpcRequest;
import com.lcx.rpc.common.RpcRequestHolder;
import com.lcx.rpc.common.RpcResponse;
import com.lcx.rpc.protocol.*;
import com.lcx.rpc.provider.registry.RegistryService;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

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
        // 构造 RPC 协议体
        RpcRequest request = new RpcRequest();
        request.setServiceVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParams(args);
        request.setParameterTypes(method.getParameterTypes());

        RpcProtocol<RpcRequest> protocol = RpcProtocol.buildProtocol(MsgType.REQUEST, request);
        long requestId = protocol.getHeader().getRequestId();
        RpcConsumer rpcConsumer = new RpcConsumer();
        RpcPromise<RpcResponse> future = new RpcPromise<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);
        // 发起 RPC 远程调用
        rpcConsumer.sendRequest(protocol, registryService);
        // 等待 RPC 调用执行结果
        // TODO hold request by ThreadLocal
        Object result = future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
        return result;
    }
}
