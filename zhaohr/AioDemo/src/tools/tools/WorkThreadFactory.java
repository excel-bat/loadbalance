package tools;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池创建参数，满足格式要求。
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class WorkThreadFactory implements ThreadFactory {
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        // int c = atomicInteger.incrementAndGet();
        // // System.out.println("create no " + c + " Threads");
        return new WorkThread(r, atomicInteger);
    }
}
