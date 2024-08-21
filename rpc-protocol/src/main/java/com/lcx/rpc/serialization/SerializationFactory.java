package com.lcx.rpc.serialization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author： lichenxu
 * @date： 2024/8/2117:36
 * @description： 序列化工厂
 * @version： v1.0
 */

@Component
@Slf4j
public class SerializationFactory implements InitializingBean {

    @Resource
    public List<AbstractRpcSerialization> rpcSerializationList;

    private static Map<SerializationTypeEnum, RpcSerialization> serializationMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        for (AbstractRpcSerialization rpcSerialization : rpcSerializationList) {
            if (rpcSerialization.getSerializationTypeEnum() != null) {
                serializationMap.putIfAbsent(rpcSerialization.getSerializationTypeEnum(), rpcSerialization);
            }
        }
    }

    public static RpcSerialization getRpcSerialization(byte serializationType) {
        SerializationTypeEnum typeEnum = SerializationTypeEnum.findByType(serializationType);
        RpcSerialization rpcSerialization = serializationMap.get(typeEnum);
        if (rpcSerialization == null) {
            throw new IllegalArgumentException("serialization type is illegal, " + serializationType);
        }
        return rpcSerialization;
    }

}
