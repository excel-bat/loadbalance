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

    public AioServer(String ip, int port) throws Exception {
        ThreadPoolExecutor threadPool = WorkThreadPool.newSingleThreadPool();
        asynChannelGroup = AsynchronousChannelGroup.withThreadPool(threadPool);
        serverSocket = AsynchronousServerSocketChannel.open(asynChannelGroup).bind(new InetSocketAddress(ip, port));
    }

    @Override
    public void run() {
        System.out.println("server start");
        try {
            serverSocket.accept(serverSocket, new AioAcceptHandler());
            // 占用cpu
            while (true) {
                long bac = 1000000;
                bac = bac >> 1;
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
