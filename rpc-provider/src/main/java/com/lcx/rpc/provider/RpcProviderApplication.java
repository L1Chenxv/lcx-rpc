package com.lcx.rpc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

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
