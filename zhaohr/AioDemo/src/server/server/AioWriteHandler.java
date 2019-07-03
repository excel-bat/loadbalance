package server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * AIO服务端write回调接口。
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class AioWriteHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {
    private ByteBuffer buffer;

    public AioWriteHandler(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void completed(Integer i, AsynchronousSocketChannel socket) {
        try {
            if (i > 0) {
                // System.out.println("writing");
                socket.write(buffer, socket, this);
            } else if (i == 0) {
                // System.out.println("write finish");
            } else if (i == -1) {
                System.out.print("write wrong");
                buffer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        exc.printStackTrace();
    }

}
