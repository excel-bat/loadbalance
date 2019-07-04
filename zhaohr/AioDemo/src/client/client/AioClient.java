package client;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import tools.WorkThreadPool;

/**
 * AIO客户端主类
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class AioClient implements Runnable {
    private static final int MAX_THREAD = 20;
    private AsynchronousChannelGroup asynChannelGroup;

    private static class ServerAddress {// 输入
        String ip;
        int port;
        String row;
        int length;

        public ServerAddress(String ip, int port, String row, int length) {
            this.ip = ip;
            this.port = port;
            this.row = row;
            this.length = length;
        }
    }

    private List<ServerAddress> serverAddress = new ArrayList<ServerAddress>();

    private class ServerInfo {// 输出
        String info;
        String ip;
        int port;
        double cpu;

        public ServerInfo(String info) {
            this.info = info;
            String[] s = info.split(":");
            this.ip = s[0];
            this.port = Integer.valueOf(s[1]);
            this.cpu = Double.valueOf(s[2]);
        }
    }

    private List<ServerInfo> serverInfo = new ArrayList<ServerInfo>();

    public AioClient(List<ServerAddress> serverAddress) throws Exception {
        ThreadPoolExecutor threadPool = WorkThreadPool.newFixedThreadPool(MAX_THREAD);
        asynChannelGroup = AsynchronousChannelGroup.withThreadPool(threadPool);
        this.serverAddress = serverAddress;
    }

    public void sortAndShowResult() {
        Collections.sort(serverInfo, new Comparator<ServerInfo>() {
            @Override
            public int compare(ServerInfo i1, ServerInfo i2) {
                int i = i1.cpu > i2.cpu ? 1 : -1;
                return i;
            }
        });
        System.out.println("排序结果：");
        for (ServerInfo info : serverInfo) {
            System.out.println(info.info);
        }
    }

    synchronized void callBackRead(String msg) {
        try {
            serverInfo.add(new ServerInfo(msg));
            if (serverInfo.size() == serverAddress.size()) {
                sortAndShowResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("client start");
            for (int i = 0; i < serverAddress.size(); i++) {
                AsynchronousSocketChannel socket = AsynchronousSocketChannel.open(asynChannelGroup);
                socket.setOption(StandardSocketOptions.TCP_NODELAY, true);
                socket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                socket.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                socket.connect(new InetSocketAddress(serverAddress.get(i).ip, serverAddress.get(i).port), socket,
                    new AioConnectHandler(this, serverAddress.get(i).row, serverAddress.get(i).length));
            }
            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally client");
        }
    }

    public static void main(String[] args) throws Exception {
        List<ServerAddress> serverAddress = new ArrayList<ServerAddress>();
        // serverAddress.add(new ServerAddress("192.168.52.135", 7007));
        // serverAddress.add(new ServerAddress("192.168.52.136", 8008));
        serverAddress.add(new ServerAddress("127.0.0.1", 8008, "r", 1000));
        serverAddress.add(new ServerAddress("127.0.0.1", 8008, "w", 1500));
        AioClient client = new AioClient(serverAddress);
        new Thread(client).start();
    }
}
