package client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * AIOread回调接口，完成读操作并通过调用AioClient.callBackRead传回数据。
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class AioReadHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {
    private ByteBuffer buffer;
    private CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
    private AioClient client;
    private String result = "";

    public AioReadHandler(ByteBuffer buffer, AioClient client) {
        this.buffer = buffer;
        this.client = client;
    }

    @Override
    public void completed(Integer i, AsynchronousSocketChannel socket) {
        try {
            if (i > 0) {
                buffer.flip();
                String msg = decoder.decode(buffer).toString();
                System.out.println("server read from " + socket.getRemoteAddress() + " message: " + msg);
                result += msg;
                socket.read(buffer, socket, this);
            } else if (i == 0) {
                client.callBackRead(socket.getRemoteAddress() + ":" + result);
            } else if (i == -1) {
                System.out.println("read wrong!");
                buffer = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        exc.printStackTrace();

    }

}
