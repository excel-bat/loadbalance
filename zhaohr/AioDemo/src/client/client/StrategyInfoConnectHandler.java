package client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import data.ServerInfo;
import strategy.StrategySelector;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class StrategyInfoConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    private ServerInfo serverInfo;
    private int i = 0;

    public StrategyInfoConnectHandler(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public StrategyInfoConnectHandler(ServerInfo serverInfo, int i) {
        this.serverInfo = serverInfo;
        this.i = i;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel socketChannel) {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                try {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    serverInfo.endTime = System.nanoTime();
                    serverInfo.setUnitTime(serverInfo.endTime - serverInfo.startTime);
                    String body;
                    body = new String(bytes, "UTF-8");
                    String[] info = body.split(":");
                    serverInfo.setCpu(Double.valueOf(info[0]));
                    serverInfo.setMemory(Double.valueOf(info[1]));
                    serverInfo.setRxBytes(Long.valueOf(info[2]));
                    serverInfo.setDev(Double.valueOf(info[3]));
                    serverInfo.setProcess(Integer.valueOf(info[4]));
                    serverInfo.setConnectAcceptedServer(Integer.valueOf(info[5]));
                    serverInfo.setConnectFinishedServer(Integer.valueOf(info[6]));
                    serverInfo.setConnectFailedServer(Integer.valueOf(info[7]));
                    serverInfo.setConnectActiveServer();
                    socketChannel.close();
                    if (ServerInfo.serverList.indexOf(serverInfo) == ServerInfo.serverList.size() - 1) {
                        StrategySelector.setWeight();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });

    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        exc.printStackTrace();
    }

}
