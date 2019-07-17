package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Random;
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
        executor = WorkThreadPool.newFixedThreadPool(1000);
        channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
        selector = new StrategySelector();
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
            try {
                if (ServerInfo.connectCountTotal - ServerInfo.connectCountFinish < 500) {
                    ServerInfo nextServer = selector.getNextServer();
                    // System.out.println(ServerInfo.serverList.indexOf(nextServer));
                    AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open(channelGroup);
                    socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                    /*socketChannel.connect(new InetSocketAddress(nextServer.ip, nextServer.filePort), socketChannel,
                        new FileConnectHandler(nextServer, random.nextInt(2), random.nextInt(7000)));*/
                    socketChannel.connect(new InetSocketAddress(nextServer.ip, nextServer.filePort), socketChannel,
                        new FileConnectHandler(nextServer, 0, 6999));
                } else {
                    // System.out.println("crowd");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                // e.printStackTrace();
            }

        }
    }

}
