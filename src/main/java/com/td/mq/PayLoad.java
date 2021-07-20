package com.td.mq;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public abstract class PayLoad implements Serializable {

    private AtomicInteger retry = new AtomicInteger(0);

    public void retryIncrement(){
        retry.getAndIncrement();
    }

    public Integer getRetry() {
        return retry.get();
    }
}