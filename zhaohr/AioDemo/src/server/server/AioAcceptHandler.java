package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import tools.CpuMonitorCalc;

/**
 * AIO服务端accept回调接口，获得cpu状态，调用write。
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
    private static final int BUFFER_SIZE = 8000;
    private static final int READ = 0;
    private static final int WRITE = 1;
    private static final int QUERY = 2;
    private static final String FILEPATH = "log/";
    AioServer server;

    public AioAcceptHandler(AioServer server) {
        this.server = server;
    }

    @Override
    public void completed(AsynchronousSocketChannel socket, AsynchronousServerSocketChannel serverSocket) {
        try {
            serverSocket.accept(serverSocket, this);
            ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            socket.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    // 连接数+1
                    server.connectCountAdd();
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String body;
                    try {
                        body = new String(bytes, "UTF-8");
                        int opr = Integer.valueOf(body.substring(0, 4));
                        String lengthString = body.substring(4, 8);
                        // int length = Integer.valueOf(body.substring(4, 8));
                        String infoString = body.substring(8);
                        if (opr == READ) {
                            // System.out.println("服务器收到读请求，来自：" + socket.getRemoteAddress().toString());
                            doRead(socket, lengthString);
                        } else if (opr == WRITE) {
                            // System.out.println("服务器收到写请求，来自：" + socket.getRemoteAddress().toString());
                            doWrite(socket, lengthString, infoString);
                        } else if (opr == QUERY) {
                            // System.out.println("服务器收到查询请求，来自：" + socket.getRemoteAddress().toString());
                            doQuery(socket);
                        } else {

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

    public void doWrite(AsynchronousSocketChannel socket, String lengthString, String infoString) {
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");
        String fileName = lengthString + "_" + df.format(new Date()) + socket.hashCode() + ".log";
        String filePathString = FILEPATH + fileName;
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(filePathString));
            out.write(infoString);
            out.close();
            doWriteBackToClient(socket, "success");
        } catch (IOException e) {
            doWriteBackToClient(socket, "failed");
            e.printStackTrace();
        }

    }

    public void doWriteBackToClient(AsynchronousSocketChannel socket, String writeString) {
        ByteBuffer writeBuffer = null;
        try {
            writeBuffer = ByteBuffer.wrap(writeString.getBytes("UTF-8"));
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

    public void doRead(AsynchronousSocketChannel socket, String lengthString) {
        File logPath = new File(FILEPATH);
        byte[] bytes = new byte[Integer.valueOf(lengthString)];
        if (logPath.isDirectory()) {
            String[] logList = logPath.list();
            // dateString包括date和后半段hashstring
            String dateString = "00000000000";
            for (String log : logList) {
                String lengthStringLocal = log.substring(0, 4);
                if (lengthStringLocal.equals(lengthString)) {
                    String dateStringLocal = log.substring(5);
                    // 查找文件规则
                    if (dateStringLocal.compareTo(dateString) > 0) {
                        dateString = dateStringLocal;
                    }
                }
            }
            String fileName = lengthString + "_" + dateString;
            // // System.out.println(fileName);
            String filePathString = FILEPATH + fileName;
            FileInputStream fis;
            if (new File(filePathString).exists()) {
                try {
                    fis = new FileInputStream(filePathString);
                    fis.read(bytes);
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ByteBuffer writeBuffer = ByteBuffer.wrap(bytes);
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

    public void doQuery(AsynchronousSocketChannel socket) {
        ByteBuffer writeBuffer = null;
        try {
            CpuMonitorCalc.getInstance().getProcessCpu();
            Thread.sleep(100);
            double cpu = CpuMonitorCalc.getInstance().getProcessCpu();
            writeBuffer = ByteBuffer.wrap(String.valueOf(cpu).getBytes("UTF-8"));
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

}