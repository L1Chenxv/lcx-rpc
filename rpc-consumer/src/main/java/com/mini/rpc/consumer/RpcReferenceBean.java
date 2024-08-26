package com.mini.rpc.consumer;

import com.lcx.rpc.provider.registry.RegistryFactory;
import com.lcx.rpc.provider.registry.RegistryService;
import com.lcx.rpc.provider.registry.RegistryType;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @description:
 * @author: L1Chenxv
 * @create: 2024-08-18 22:41
 */

public class RpcReferenceBean implements FactoryBean<Object> {

    private Class<?> interfaceClass;

    private String serviceVersion;

    private String registryType;

    private String registryAddr;

    private long timeout;

    private Object object;

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    /**
     * BeanDefinition 初始化方法
     * @throws Exception
     */
    public void init() throws Exception {
        // TODO 生成动态代理对象并赋值给 object
        RegistryService registryService = RegistryFactory.getInstance(this.registryAddr, RegistryType.ZOOKEEPER);
        this.object = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[] {interfaceClass},
                new RpcInvokerProxy(serviceVersion, timeout, registryService));
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
