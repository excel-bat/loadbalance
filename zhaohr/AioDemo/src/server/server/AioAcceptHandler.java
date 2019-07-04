package server;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import tools.CpuMonitorCalc;

/**
 * AIO服务端accept回调接口，获得cpu状态，调用write。
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
    private static final int BUFFER_SIZE = 8192;
    private static final String READ = "r";
    private static final String WRITE = "w";
    private static final String LENGTH = "l";
    AioServer server;

    public AioAcceptHandler(AioServer server) {
        this.server = server;
    }

    @Override
    public void completed(AsynchronousSocketChannel socket, AsynchronousServerSocketChannel serverSocket) {
        try {
            serverSocket.accept(serverSocket, this);
            System.out.println("连接至" + socket.getRemoteAddress().toString());
            ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            socket.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    server.connectCountAdd();
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String body;
                    try {
                        body = new String(bytes, "UTF-8");
                        if (body.contains(LENGTH)) {
                            String row = body.substring(0, 1);
                            int length = Integer.valueOf(body.substring(1, body.indexOf("l")));
                            if (READ.equals(row)) {
                                System.out.println("服务器收到读请求");
                                doWrite(socket, length);
                            } else if (WRITE.equals(row)) {
                                System.out.println("服务器收到写请求");
                                doRead(socket, length);
                            } else {

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        exc.printStackTrace();

    }

    public void doWrite(AsynchronousSocketChannel socket, int length) {
        ByteBuffer writeBuffer = null;
        try {
            CpuMonitorCalc.getInstance().getProcessCpu();
            Thread.sleep(100);
            double cpu = CpuMonitorCalc.getInstance().getProcessCpu();
            byte[] bytes = new byte[length];
            System.arraycopy(String.valueOf(cpu).getBytes("UTF-8"), 0, bytes, 0, String.valueOf(cpu).length());
            writeBuffer = ByteBuffer.wrap(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining()) {
                    socket.write(buffer, buffer, this);
                } else {
                }

            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

    public void doRead(AsynchronousSocketChannel socket, int length) {
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
                    System.out.println("服务器收到文件写入：" + body);
                    String reply = body.length() == length ? "SUCCESS" : "FAILURE";
                    doWriteBackToClient(socket, reply);
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

    public void doWriteBackToClient(AsynchronousSocketChannel socket, String reply) {
        ByteBuffer writeBuffer = null;
        try {

            writeBuffer = ByteBuffer.wrap(reply.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        socket.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining()) {
                    socket.write(buffer, buffer, this);
                } else {

                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

}
