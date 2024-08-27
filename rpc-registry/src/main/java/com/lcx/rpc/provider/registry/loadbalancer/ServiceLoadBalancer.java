package com.lcx.rpc.provider.registry.loadbalancer;

import java.util.List;

public interface ServiceLoadBalancer<T> {
    T select(List<T> servers, String serviceName, int hashCode);

    void removeRing(String serviceName);
}