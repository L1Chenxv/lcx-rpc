package com.lcx.rpc.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author： lichenxu
 * @date： 2024/8/2818:25
 * @description：心跳数据
 * @version： v1.0
 */

@Data
public class HeartBeatData implements Serializable {
    private Object data;
    private String message;
}
