package strategy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import client.AioConnectHandler;
import data.ServerInfo;
import tools.WorkThreadPool;

/**
 * @author zhaohr16
 * @date 2019/07/08
 */
public class StrategySelector {
    private List<ServerInfo> serverList;
    private AsynchronousChannelGroup asynChannelGroup;
    public boolean dataIsReady;
    Strategy strategy;

    public StrategySelector(List<ServerInfo> serverList) throws IOException {
        this.serverList = serverList;
        ThreadPoolExecutor threadPool = WorkThreadPool.newFixedThreadPool(serverList.size());
        asynChannelGroup = AsynchronousChannelGroup.withThreadPool(threadPool);
        dataIsReady = false;
        strategy = new RandomStrategy();
    }

    public ServerInfo getNextServer() throws InterruptedException {
        getServerInfo();
        while (!dataIsReady) {
            Thread.sleep(100);
        }
        dataIsReady = false;
        return strategy.getNextServer(serverList);
    }

    public void getServerInfo() {
        for (ServerInfo serverInfo : serverList) {
            try {
                AsynchronousSocketChannel socket = AsynchronousSocketChannel.open(asynChannelGroup);
                socket.setOption(StandardSocketOptions.TCP_NODELAY, true);
                socket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                socket.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                if (serverList.indexOf(serverInfo) == serverList.size() - 1) {
                    socket.connect(new InetSocketAddress(serverInfo.ip, serverInfo.port), socket,
                        new AioConnectHandler(this, serverInfo, 2, 1));
                } else {
                    socket.connect(new InetSocketAddress(serverInfo.ip, serverInfo.port), socket,
                        new AioConnectHandler(this, serverInfo, 2, 0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
