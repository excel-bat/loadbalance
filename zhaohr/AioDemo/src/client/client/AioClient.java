package client;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import data.ServerInfo;
import strategy.StrategySelector;
import tools.WorkThreadPool;

/**
 * AIO客户端主类
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class AioClient implements Runnable {
    private static final int MAX_THREAD = 1000;
    private AsynchronousChannelGroup asynChannelGroup;
    private static final int READ = 0;
    private static final int WRITE = 1;

    private List<ServerInfo> serverList = new ArrayList<ServerInfo>();
    private StrategySelector selector;

    public AioClient(List<ServerInfo> serverList) throws Exception {
        ThreadPoolExecutor threadPool = WorkThreadPool.newFixedThreadPool(MAX_THREAD);
        asynChannelGroup = AsynchronousChannelGroup.withThreadPool(threadPool);
        this.serverList = serverList;
        this.selector = new StrategySelector(serverList);
    }

    /*   public void sortAndShowResult() {
        Collections.sort(serverInfo, new Comparator<ServerInfo>() {
            @Override
            public int compare(ServerInfo i1, ServerInfo i2) {
                int i = i1.cpu > i2.cpu ? 1 : -1;
                return i;
            }
        });
        // System.out.println("排序结果：");
        for (ServerInfo info : serverInfo) {
            // System.out.println(info.info);
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
    }*/

    @Override
    public void run() {
        try {
            // System.out.println("client start");
            while (true) {
                ServerInfo nextServer = selector.getNextServer();
                System.out.println(serverList.indexOf(nextServer));
                AsynchronousSocketChannel socket = AsynchronousSocketChannel.open(asynChannelGroup);
                socket.connect(new InetSocketAddress(nextServer.ip, nextServer.port), socket,
                    new AioConnectHandler(READ, 1000));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // System.out.println("finally client");
        }
    }

    public static void main(String[] args) throws Exception {
        List<ServerInfo> serverList = new ArrayList<ServerInfo>();
        // serverList.add(new ServerInfo("192.168.52.135", 8000));
        serverList.add(new ServerInfo("192.168.52.137", 8000));
        // serverList.add(new ServerInfo("127.0.0.1", 8000));

        AioClient client = new AioClient(serverList);
        new Thread(client).start();
    }
}
