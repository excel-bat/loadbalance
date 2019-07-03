package tools;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池封装类，满足格式要求。
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class WorkThreadPool {
    public static ThreadPoolExecutor newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new WorkThreadFactory());
    }

    public static ThreadPoolExecutor newSingleThreadPool() {
        return newFixedThreadPool(1);
    }

}
