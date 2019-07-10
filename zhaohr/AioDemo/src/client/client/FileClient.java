package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

import data.ServerInfo;
import strategy.StrategySelector;
import tools.WorkThreadPool;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class FileClient implements Runnable {

    private ThreadPoolExecutor executor;
    private AsynchronousChannelGroup channelGroup;
    private StrategySelector selector;

    public FileClient() throws IOException {
        executor = WorkThreadPool.newFixedThreadPool(ServerInfo.serverList.size());
        channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
        selector = new StrategySelector();
    }

    @Override
    public void run() {
        while (true) {
            try {
                ServerInfo nextServer = selector.getNextServer();
                System.out.println(ServerInfo.serverList.indexOf(nextServer));
                AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open(channelGroup);
                socketChannel.connect(new InetSocketAddress(nextServer.ip, nextServer.filePort), socketChannel,
                    new FileConnectHandler(0, 1000));
                // windows
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
