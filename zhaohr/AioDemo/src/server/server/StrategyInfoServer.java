package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

import tools.WorkThreadPool;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class StrategyInfoServer implements Runnable {
    private String ip;
    private int port;
    private ThreadPoolExecutor executor;
    private AsynchronousChannelGroup channelGroup;

    public StrategyInfoServer(String ip, int port) throws IOException {
        executor = WorkThreadPool.newFixedThreadPool(10);
        channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        AsynchronousServerSocketChannel serverSocketChannel;
        try {
            serverSocketChannel =
                AsynchronousServerSocketChannel.open(channelGroup).bind(new InetSocketAddress(ip, port));
            serverSocketChannel.accept(serverSocketChannel, new StrategyInfoAcceptHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
