package server;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.sun.management.OperatingSystemMXBean;

import tools.WorkThreadPool;

/**
 * AIO服务端主类
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class AioServer implements Runnable {
    private AsynchronousChannelGroup asynChannelGroup;
    private AsynchronousServerSocketChannel serverSocket;
    private static ThreadPoolExecutor threadPool;
    // 总连接数
    private int connectCountTotal = 0;
    // 当前连接数
    private int connectCountNow = 0;
    public static Logger logger = Logger.getLogger(AioServer.class);

    public AioServer(String ip, int port) throws Exception {
        // threadPool = WorkThreadPool.newSingleThreadPool();
        threadPool = WorkThreadPool.newFixedThreadPool(5);
        asynChannelGroup = AsynchronousChannelGroup.withThreadPool(threadPool);
        serverSocket = AsynchronousServerSocketChannel.open(asynChannelGroup).bind(new InetSocketAddress(ip, port));
    }

    public synchronized void connectCountTotalAdd() {
        connectCountTotal++;
    }

    public synchronized void connectCountNowAdd() {
        connectCountNow++;
    }

    public synchronized void connectCountNowSub() {
        connectCountNow--;
    }

    public synchronized int getConnectCountNow() {
        return connectCountNow;
    }

    @Override
    public void run() {
        logger.info("SERVER START");
        try {
            serverSocket.accept(serverSocket, new AioAcceptHandler(this));
            while (true) {
                logger.info("服务端接收请求数： " + connectCountTotal);
                OperatingSystemMXBean osMxBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
                double cpu = osMxBean.getSystemCpuLoad();
                logger.info("当前cpu占用率： " + cpu);
                // logger.info("服务端当前请求数： " + connectCountNow);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // System.out.println("finally server");
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(new AioServer("127.0.0.1", 8000)).start();
    }
}
