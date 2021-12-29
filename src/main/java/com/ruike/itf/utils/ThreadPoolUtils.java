package com.ruike.itf.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 线程池工具包
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/16 09:47
 */
public class ThreadPoolUtils {
    private static final ExecutorService THREAD_POOL;

    static {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int maxPoolSize = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 5;
        TimeUnit keepAliveTimeUnit = TimeUnit.MINUTES;
        int queSize = 1024;
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-pool-%d").build();
        THREAD_POOL = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, keepAliveTimeUnit, new ArrayBlockingQueue<>(queSize)
                , threadFactory);
    }

    /**
     * 获取线程池
     *
     * @return 线程池
     */
    public static ExecutorService getThreadPool() {
        return THREAD_POOL;
    }


}