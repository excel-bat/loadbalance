package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import tools.WorkThreadPool;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class FileServer implements Runnable {
    private String ip;
    private int port;
    private ThreadPoolExecutor executor;
    private AsynchronousChannelGroup channelGroup;
    private Logger logger = Logger.getLogger(FileServer.class);

    public FileServer(String ip, int port) throws IOException {
        executor = WorkThreadPool.newFixedThreadPool(100);
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
            serverSocketChannel.accept(serverSocketChannel, new FileAcceptHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            StrategyInfo.setConnectCountActive(executor.getActiveCount());
        }

    }

}
