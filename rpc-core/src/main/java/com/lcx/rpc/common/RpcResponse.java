package com.lcx.rpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author： lichenxu
 * @date： 2024/8/2018:49
 * @description： RPC Response
 * @version： v1.0
 */

@Data
public class RpcResponse implements Serializable {
    private Object data;

    private String message;

}
