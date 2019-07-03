package client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * AIO客户端connect回调接口，连接后调用read。
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class AioConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    private ByteBuffer buffer;
    private static final int BUFFER_SIZE = 20;
    private AioClient client;

    public AioConnectHandler(AioClient client) {
        this.client = client;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel socket) {
        try {
            buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.clear();
            socket.read(buffer, socket, new AioReadHandler(buffer, client));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        exc.printStackTrace();
    }
}
