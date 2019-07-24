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

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class FileAcceptHandler
    implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private static final int BUFFER_SIZE = 8000;
    private static final int READ = 0;
    private static final int WRITE = 1;
    private static final String FILEPATH = "log/";

    @Override
    public void completed(AsynchronousSocketChannel socketChannel,
        AsynchronousServerSocketChannel serverSocketChannel) {
        serverSocketChannel.accept(serverSocketChannel, this);
        StrategyInfo.addConnectAccepted();
        ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        socketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                try {
                    String body = new String(bytes, "UTF-8");
                    int opr = Integer.valueOf(body.substring(0, 4));
                    String lengthString = body.substring(4, 8);
                    // int length = Integer.valueOf(body.substring(4, 8));
                    String infoString = body.substring(8);
                    if (opr == READ) {
                        // System.out.println("服务器收到读请求，来自：" + socketChannel.getRemoteAddress().toString());
                        doRead(socketChannel, lengthString);
                    } else if (opr == WRITE) {
                        // System.out.println("服务器收到写请求，来自：" + socketChannel.getRemoteAddress().toString());
                        doWrite(socketChannel, lengthString, infoString);
                    } else {

                    }
                } catch (Exception e) {
                    StrategyInfo.addConnectFailed();
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                StrategyInfo.addConnectFailed();
                exc.printStackTrace();
            }
        });

    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        StrategyInfo.addConnectFailed();
        exc.printStackTrace();
    }

    public void doWrite(AsynchronousSocketChannel socketChannel, String lengthString, String infoString) {
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");
        String fileName = lengthString + "_" + df.format(new Date()) + socketChannel.hashCode() + ".log";
        String filePathString = FILEPATH + fileName;
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(filePathString));
            out.write(infoString);
            out.close();
            doWriteBackToClient(socketChannel, "success");
        } catch (IOException e) {
            doWriteBackToClient(socketChannel, "failed");
            e.printStackTrace();
        }

    }

    public void doWriteBackToClient(AsynchronousSocketChannel socketChannel, String writeString) {
        ByteBuffer writeBuffer = null;
        try {
            byte[] bytes = new byte[1024];
            System.arraycopy(writeString.getBytes("UTF-8"), 0, bytes, 0, writeString.length());
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
                        StrategyInfo.addConnectFinished();
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                StrategyInfo.addConnectFailed();
                exc.printStackTrace();
            }
        });
    }

    public void doRead(AsynchronousSocketChannel socketChannel, String lengthString) {
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
        socketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining()) {
                    socketChannel.write(buffer, buffer, this);
                } else {
                    try {
                        StrategyInfo.addConnectFinished();
                        socketChannel.close();
                    } catch (IOException e) {
                        StrategyInfo.addConnectFailed();
                    }

                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                StrategyInfo.addConnectFailed();
                exc.printStackTrace();
            }
        });

    }
}
