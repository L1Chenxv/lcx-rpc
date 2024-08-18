package com.lcx.rpc.provider;

import com.lcx.rpc.provider.registry.RegistryService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author： lichenxu
 * @date： 2024/8/1817:29
 * @description：
 * @version： v1.0
 */

@Slf4j
public class RpcProvider {

    private String serverAddress;
    private final int serverPort;
    private final RegistryService serviceRegistry;

    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    public RpcProvider(int serverPort, RegistryService serviceRegistry) {
        this.serverPort = serverPort;
        this.serviceRegistry = serviceRegistry;
    }
}
