package tools;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程封装类，满足格式要求。
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class WorkThread extends Thread {
    private Runnable target;
    private AtomicInteger counter;

    public WorkThread(Runnable target, AtomicInteger counter) {
        this.target = target;
        this.counter = counter;
    }

    @Override
    public void run() {
        try {
            target.run();
        } finally {
            int c = counter.getAndDecrement();
            // System.out.println("terminate no " + c + " Threads");
        }
    }
}