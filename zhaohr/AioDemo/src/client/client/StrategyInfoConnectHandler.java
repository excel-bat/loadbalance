package client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class StrategyInfoConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    private ServerInfo serverInfo;

    public StrategyInfoConnectHandler(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
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
                    String body;
                    body = new String(bytes, "UTF-8");
                    serverInfo.setCpu(Double.valueOf(body));
                    socketChannel.close();
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
