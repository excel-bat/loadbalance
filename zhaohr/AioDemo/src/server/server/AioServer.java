package server;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

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
    private static int connectCount = 0;

    public AioServer(String ip, int port) throws Exception {
        // threadPool = WorkThreadPool.newSingleThreadPool();
        threadPool = WorkThreadPool.newFixedThreadPool(5);
        asynChannelGroup = AsynchronousChannelGroup.withThreadPool(threadPool);
        serverSocket = AsynchronousServerSocketChannel.open(asynChannelGroup).bind(new InetSocketAddress(ip, port));
    }

    synchronized void connectCountAdd() {
        connectCount++;
    }

    @Override
    public void run() {
        System.out.println("server start");
        try {
            serverSocket.accept(serverSocket, new AioAcceptHandler(this));
            while (true) {
                System.out.println("服务端接收请求数： " + connectCount);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally server");
        }
    }

    public static void main(String[] args) throws Exception {
        AioServer server = new AioServer("127.0.0.1", 9009);
        new Thread(server).start();
    }
}
