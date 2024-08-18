package com.lcx.rpc.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author： lichenxu
 * @date： 2024/8/1817:23
 * @description：
 * @version： v1.0
 */
@Data
@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {

    private int servicePort;

    private String registryAddr;

    private String registryType;
}
