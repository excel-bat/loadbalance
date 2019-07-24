package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.text.DecimalFormat;
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

    public FileClient() throws IOException {
        executor = WorkThreadPool.newFixedThreadPool(1000);
        channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
    }

    private static double getPossionProbability(int k, double lamda) {
        double c = Math.exp(-lamda), sum = 1;
        for (int i = 1; i <= k; i++) {
            sum *= lamda / i;
        }
        return sum * c;
    }

    private static int getPossionVariable(double lamda) {
        int x = 0;
        double y = Math.random(), cdf = getPossionProbability(x, lamda);
        while (cdf < y) {
            x++;
            cdf += getPossionProbability(x, lamda);
        }
        return x;
    }

    @Override
    public void run() {
        int i = 0;
        int countTotal = 0;
        long time = System.currentTimeMillis();

        while (true) {
            int count = 60 * getPossionVariable(50);
            if (i > 300) {
                count = 0;
            }
            int countPrint = count;

            System.out.println(System.currentTimeMillis() - time);
            System.out.println(i + "  " + countPrint);
            System.out.println(
                ServerInfo.getConnectFinishedTotal() + "/" + ServerInfo.getConnectSendTotal() + "/" + countTotal + " "
                    + new DecimalFormat("0.000%").format((double)ServerInfo.getConnectSendTotal() / countTotal));
            for (ServerInfo serverInfo : ServerInfo.serverList) {
                double connectClientWrongRate = (double)serverInfo.getConnectFailedClient()
                    / (serverInfo.getConnectFailedClient() + serverInfo.getConnectFinishedClient());
                double connectServerWrongRate = (double)serverInfo.getConnectFailedServer()
                    / (serverInfo.getConnectFailedServer() + serverInfo.getConnectFinishedServer());
                System.out.println(serverInfo.getConnectFinishedClient() + "/" + serverInfo.getConnectFailedClient()
                    + "/" + serverInfo.getConnectSendClient() + " " + serverInfo.getConnectFinishedServer() + "/"
                    + serverInfo.getConnectFailedServer() + "/" + serverInfo.getConnectAcceptedServer() + " "
                    + new DecimalFormat("0.000%").format(connectClientWrongRate) + " "
                    + new DecimalFormat("0.000%").format(connectServerWrongRate));
            }
            System.out.println();

            time = System.currentTimeMillis();
            countTotal += count;
            count++;
            i++;
            while (System.currentTimeMillis() - time < 1000) {
                if (count-- > 0) {
                    try {
                        if (ServerInfo.getConnectSendTotal() - ServerInfo.getConnectFinishedTotal()
                            - ServerInfo.getConnectFailedTotal() < 1000) {
                            ServerInfo nextServer = StrategySelector.getNextServer();
                            nextServer.addConnectSendClient();
                            // System.out.println(ServerInfo.serverList.indexOf(nextServer));
                            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open(channelGroup);
                            socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                            socketChannel.connect(new InetSocketAddress(nextServer.ip, nextServer.filePort),
                                socketChannel, new FileConnectHandler(nextServer, 0, 6999));
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {

                    }
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        /* while (true) {
            try {
                if (ServerInfo.connectSendTotal - ServerInfo.connectFinishedTotal
                    - ServerInfo.connectFailedTotal < 1000) {
                    ServerInfo nextServer = selector.getNextServer();
                    nextServer.connectSend++;
                    ServerInfo.connectSendTotal++;
                    // System.out.println(ServerInfo.serverList.indexOf(nextServer));
                    AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open(channelGroup);
                    socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                    socketChannel.connect(new InetSocketAddress(nextServer.ip, nextServer.filePort), socketChannel,
                        new FileConnectHandler(nextServer, 0, 6999));
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
        
            }
        }*/
    }

}
