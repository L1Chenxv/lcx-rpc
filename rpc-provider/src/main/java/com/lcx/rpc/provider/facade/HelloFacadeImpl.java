package com.lcx.rpc.provider.facade;


import com.lcx.rpc.provider.annotation.RpcService;

@RpcService(serviceInterface = HelloFacade.class, serviceVersion = "1.0.0")
public class HelloFacadeImpl implements HelloFacade {
    @Override
    public String hello(String name) {
        return "hello" + name;
    }
}
