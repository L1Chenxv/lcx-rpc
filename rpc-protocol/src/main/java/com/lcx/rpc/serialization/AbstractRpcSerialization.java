package com.lcx.rpc.serialization;

import lombok.Data;

import java.io.IOException;

/**
 * @author： lichenxu
 * @date： 2024/8/2117:40
 * @description： 序列化抽象
 * @version： v1.0
 */

@Data
public abstract class AbstractRpcSerialization implements RpcSerialization {

    /**
     * 序列化类型
     */
    public SerializationTypeEnum serializationTypeEnum;

    @Override
    public abstract <T> byte[] serialize(T obj) throws IOException;

    @Override
    public abstract <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
