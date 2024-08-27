package com.lcx.rpc.provider.registry.loadbalancer;


import com.lcx.rpc.common.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class ZKConsistentHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<ServiceMeta>> {
    private final static int VIRTUAL_NODE_SIZE = 10;
    private final static String VIRTUAL_NODE_SPLIT = "#";

    private Map<String, TreeMap<Integer, ServiceInstance<ServiceMeta>>> instanceMap = new TreeMap<>();

    @Override
    public ServiceInstance<ServiceMeta> select(List<ServiceInstance<ServiceMeta>> servers, String serviceName, int hashCode) {
        // 哈希环持久化
        if (CollectionUtils.isEmpty(instanceMap.get(serviceName))) {
            synchronized (ZKConsistentHashLoadBalancer.class) {
                if (CollectionUtils.isEmpty(instanceMap.get(serviceName))) {
                    TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = makeConsistentHashRing(servers);
                    instanceMap.put(serviceName, ring);
                }
            }
        }

        return allocateNode(instanceMap.get(serviceName), hashCode);

    }

    @Override
    public void removeRing(String serviceName) {
         instanceMap.remove(serviceName);
    }

    /**
     * 分配节点
     * @param ring
     * @param hashCode
     * @return
     */
    private ServiceInstance<ServiceMeta> allocateNode(TreeMap<Integer, ServiceInstance<ServiceMeta>> ring, int hashCode) {
        Map.Entry<Integer, ServiceInstance<ServiceMeta>> entry = ring.ceilingEntry(hashCode);
        if (entry == null) {
            entry = ring.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * 构造哈希环
     * @param servers
     * @return
     */
    private TreeMap<Integer, ServiceInstance<ServiceMeta>> makeConsistentHashRing(List<ServiceInstance<ServiceMeta>> servers) {
        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = new TreeMap<>();
        for (ServiceInstance<ServiceMeta> instance : servers) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                ring.put((buildServiceInstanceKey(instance) + VIRTUAL_NODE_SPLIT + i).hashCode(), instance);
            }
        }
        return ring;
    }

    private String buildServiceInstanceKey(ServiceInstance<ServiceMeta> instance) {
        ServiceMeta payload = instance.getPayload();
        return String.join(":", payload.getServiceAddr(), String.valueOf(payload.getServicePort()));
    }

}
