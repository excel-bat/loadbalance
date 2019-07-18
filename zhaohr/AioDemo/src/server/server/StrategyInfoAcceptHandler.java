package server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class StrategyInfoAcceptHandler
    implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    /* (non-Javadoc)
     * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
     */
    @Override
    public void completed(AsynchronousSocketChannel socketChannel,
        AsynchronousServerSocketChannel serverSocketChannel) {
        serverSocketChannel.accept(serverSocketChannel, this);
        ByteBuffer writeBuffer = null;
        try {
            byte[] bytes = new byte[1024];
            int connectCurrent =
                StrategyInfo.connectAccepted - StrategyInfo.connectFinished - StrategyInfo.connectFailed;
            double wrongRate =
                (double)StrategyInfo.connectFailed / (StrategyInfo.connectFailed + StrategyInfo.connectFinished);
            String message = String.valueOf(StrategyInfo.getCpu()) + ":" + String.valueOf(connectCurrent) + ":"
                + String.valueOf(wrongRate) + ":";
            System.arraycopy(message.getBytes("UTF-8"), 0, bytes, 0, message.length());;
            writeBuffer = ByteBuffer.wrap(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        socketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                try {
                    if (buffer.hasRemaining()) {
                        socketChannel.write(buffer, buffer, this);
                    } else {
                        socketChannel.close();
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
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        exc.printStackTrace();
    }
}
