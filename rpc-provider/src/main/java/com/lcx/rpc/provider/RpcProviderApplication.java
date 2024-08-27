package com.lcx.rpc.provider;

import com.lcx.rpc.protocol.MsgType;
import com.lcx.rpc.serialization.SerializationFactory;
import com.lcx.rpc.serialization.SerializationTypeEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @description:
 * @author: L1Chenxv
 * @create: 2024-08-18 21:13
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.lcx.rpc"})
public class RpcProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(RpcProviderApplication.class, args);
    }
}
