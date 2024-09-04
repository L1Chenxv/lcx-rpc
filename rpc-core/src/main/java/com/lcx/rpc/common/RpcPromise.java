package com.lcx.rpc.common;

import io.netty.util.concurrent.Promise;
import lombok.Data;

@Data
public class RpcPromise<T> {
    private Promise<T> promise;
    private long timeout;

    public RpcPromise(Promise<T> promise, long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}
