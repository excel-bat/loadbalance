package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    private static final int BUFFER_SIZE = 8192;
    private AioClient client;
    private String row;// read or write
    private int length;
    private static final String FILEPATH = "D:\\work\\eclipse-workspace\\test.txt";

    public AioConnectHandler(AioClient client, String row, int length) {
        this.client = client;
        this.row = row;
        this.length = length;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel socket) {
        // 连接后逻辑
        // 按格式生成包
        ByteBuffer writeBuffer = null;
        try {
            byte[] bytes = new byte[BUFFER_SIZE];
            String message = row + String.valueOf(length) + "l";
            // message = String.format("%-" + BUFFER_SIZE + "s", message).replace(' ', '0');
            System.arraycopy(message.getBytes("UTF-8"), 0, bytes, 0, message.length());
            writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        socket.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining()) {
                    socket.write(buffer, buffer, this);
                } else {
                    if (row.equals("r")) {
                        System.out.println("客户端发送读请求");
                        doRead(socket);
                    } else if (row.equals("w")) {
                        System.out.println("客户端发送写请求");
                        doWrite(socket);
                    } else {
                        System.out.println("客户端发送标识错误");
                        // rw标识错误
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

    public void doRead(AsynchronousSocketChannel socket) {
        ByteBuffer readBuffer = ByteBuffer.allocate(length);
        socket.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String body;
                try {
                    body = new String(bytes, "UTF-8");
                    // cpu统计
                    System.out.println("客户端接收数据（cpu占用率）：" + body);
                    client.callBackRead(socket.getRemoteAddress() + ":" + body);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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

    public void doWrite(AsynchronousSocketChannel socket) {
        try {
            FileInputStream fis = new FileInputStream(FILEPATH);
            byte[] bytes = new byte[length];
            while (fis.read(bytes) != -1) {
                ByteBuffer writeBuffer = ByteBuffer.wrap(bytes);
                socket.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

                    @Override
                    public void completed(Integer result, ByteBuffer buffer) {
                        if (buffer.hasRemaining()) {
                            socket.write(buffer, buffer, this);
                        } else {
                            doReadReply(socket);
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        exc.printStackTrace();
                    }
                });
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doReadReply(AsynchronousSocketChannel socket) {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        socket.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String body;
                try {
                    body = new String(bytes, "UTF-8");
                    System.out.println("客户端收到服务端反馈：" + body);
                } catch (UnsupportedEncodingException e) {
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
