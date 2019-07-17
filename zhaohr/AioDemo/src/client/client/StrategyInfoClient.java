package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

import data.ServerInfo;
import tools.WorkThreadPool;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class StrategyInfoClient implements Runnable {
    private ThreadPoolExecutor executor;
    private AsynchronousChannelGroup channelGroup;

    public StrategyInfoClient() throws IOException {
        executor = WorkThreadPool.newFixedThreadPool(10);
        channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (ServerInfo serverInfo : ServerInfo.serverList) {
                    serverInfo.startTime = System.nanoTime();
                    AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open(channelGroup);
                    socketChannel.connect(new InetSocketAddress(serverInfo.ip, serverInfo.infoPort), socketChannel,
                        new StrategyInfoConnectHandler(serverInfo));
                }
                Thread.sleep(500);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
