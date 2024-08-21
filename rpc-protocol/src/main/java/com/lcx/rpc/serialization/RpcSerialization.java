package com.lcx.rpc.serialization;

import java.io.IOException;

/**
 * @author： lichenxu
 * @date： 2024/8/2018:52
 * @description： 序列化接口
 * @version： v1.0
 */
public interface RpcSerialization {

    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
