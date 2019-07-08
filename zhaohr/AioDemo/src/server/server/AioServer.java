package server;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

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
    public static Logger logger = Logger.getLogger(AioServer.class);

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
        logger.info("SERVER START");
        try {
            serverSocket.accept(serverSocket, new AioAcceptHandler(this));
            while (true) {
                logger.info("服务端接收请求数： " + connectCount);
                try {
                    Thread.sleep(5000);
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
        AioServer server = new AioServer("127.0.0.1", 9009);
        new Thread(server).start();

        /*AioServer server1 = new AioServer("127.0.0.1", 8008);
        new Thread(server1).start();
        AioServer server11 = new AioServer("127.0.0.1", 7007);
        new Thread(server11).start();*/
    }
}
