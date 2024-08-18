package com.lcx.rpc.provider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author: L1Chenxv
 * @create: 2024-08-18 21:43
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcService {

    Class<?> serviceInterface() default Object.class;

    String serviceVersion() default "1.0";
}
