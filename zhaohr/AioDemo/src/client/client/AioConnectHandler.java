package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import data.ServerInfo;
import strategy.StrategySelector;

/**
 * AIO客户端connect回调接口，连接后调用read。
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class AioConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    private static final int BUFFER_SIZE = 8000;
    private static final int READ = 0;
    private static final int WRITE = 1;
    private static final int QUERY = 2;

    private int opr;
    private int length;
    private ServerInfo serverInfo;
    private StrategySelector selector;

    public AioConnectHandler(int opr, int length) {

        this.opr = opr;
        this.length = length;
    }

    public AioConnectHandler(StrategySelector selector, ServerInfo serverInfo, int opr, int length) {
        this.selector = selector;
        this.serverInfo = serverInfo;
        this.opr = opr;
        this.length = length;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel socket) {
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
        socket.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining()) {
                    socket.write(buffer, buffer, this);
                } else {
                    try {
                        if (opr == READ) {
                            // System.out.println("客户端发送读请求到：" + socket.getRemoteAddress().toString());
                            doRead(socket);
                        } else if (opr == WRITE) {
                            // System.out.println("客户端发送写请求到：" + socket.getRemoteAddress().toString());
                            doWrite(socket);
                        } else if (opr == QUERY) {
                            // System.out.println("客户端发送查询请求到：" + socket.getRemoteAddress().toString());
                            doQuery(socket);
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
                    // System.out.println("从" + socket.getRemoteAddress().toString() + "读到：" + body);
                    // 当返回cpu占用率时调用并统计
                    // client.callBackRead(socket.getRemoteAddress() + ":" + body);
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
                    // System.out.println("到" + socket.getRemoteAddress().toString() + "的写反馈：" + body);
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

    public void doQuery(AsynchronousSocketChannel socket) {
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
                    // System.out.println("查询到" + socket.getRemoteAddress().toString() + "的信息：" + body);
                    serverInfo.cpu = Double.valueOf(body);
                    if (length == 1) {
                        selector.dataIsReady = true;
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
}
