package com.lcx.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author： lichenxu
 * @date： 2024/8/2018:38
 * @description： 协议
 * @version： v1.0
 */

@Data
public class RpcProtocol<T> implements Serializable {

    private MsgHeader header;

    private T body;
}
