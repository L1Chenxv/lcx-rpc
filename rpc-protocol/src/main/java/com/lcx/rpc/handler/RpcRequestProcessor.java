package com.lcx.rpc.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author： lichenxu
 * @date： 2024/8/2314:24
 * @description： RPC 异步处理
 * @version： v1.0
 */
public class RpcRequestProcessor {

    private static ThreadPoolExecutor threadPoolExecutor;

    public static void submitRequest(Runnable task) {
        if (threadPoolExecutor == null) {
            synchronized (RpcRequestProcessor.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));
                }
            }
        }
        threadPoolExecutor.submit(task);
    }
}
