package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class FileConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    private static final int BUFFER_SIZE = 8000;
    private static final int READ = 0;
    private static final int WRITE = 1;
    private int opr;
    private int length;

    public FileConnectHandler(int opr, int length) {
        this.opr = opr;
        this.length = length;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel socketChannel) {
        // 连接后逻辑
        // 按格式生成包 4-4-7992
        ByteBuffer writeBuffer = null;
        try {
            byte[] bytes = new byte[BUFFER_SIZE];
            String oprString = String.format("%4s", String.valueOf(opr)).replace(" ", "0");
            String lengthString = String.format("%4s", String.valueOf(length)).replace(" ", "0");
            String infoString = null;
            if (opr == WRITE) {
                infoString = String.format("%" + String.valueOf(BUFFER_SIZE - 8) + "s", "0").replace(" ", "0");
            }
            String message = oprString + lengthString + infoString;
            System.arraycopy(message.getBytes("UTF-8"), 0, bytes, 0, message.length());
            writeBuffer = ByteBuffer.wrap(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        socketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining()) {
                    socketChannel.write(buffer, buffer, this);
                } else {
                    try {
                        if (opr == READ) {
                            // System.out.println("客户端发送读请求到：" + socketChannel.getRemoteAddress().toString());
                            doRead(socketChannel);
                        } else if (opr == WRITE) {
                            // System.out.println("客户端发送写请求到：" + socketChannel.getRemoteAddress().toString());
                            doWrite(socketChannel);
                        } else {
                            // System.out.println("客户端发送标识错误");
                            // rw标识错误
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    public void doRead(AsynchronousSocketChannel socketChannel) {
        ByteBuffer readBuffer = ByteBuffer.allocate(length);
        socketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String body;
                try {
                    body = new String(bytes, "UTF-8");
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

    public void doWrite(AsynchronousSocketChannel socketChannel) {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String body;
                try {
                    body = new String(bytes, "UTF-8");
                    // System.out.println("到" + socketChannel.getRemoteAddress().toString() + "的写反馈：" + body);

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
}
