package client;  

import java.io.IOException;  
import java.nio.ByteBuffer;  
import java.nio.channels.AsynchronousSocketChannel;  
import java.nio.channels.CompletionHandler;  
import java.util.concurrent.CountDownLatch;

import monitor.tool.ServerInfo;  

/**
 * WriteHandler class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {  
    private AsynchronousSocketChannel clientChannel;  
    private CountDownLatch latch;  
    private int id;
    private ServerInfo sInfo;
    public WriteHandler(AsynchronousSocketChannel clientChannel,CountDownLatch latch, int id, ServerInfo sInfo) {  
        this.clientChannel = clientChannel;  
        this.latch = latch;  
        this.id = id;
        this.sInfo = sInfo;
    }  
    @Override  
    public void completed(Integer result, ByteBuffer buffer) {  
        //完成全部数据的写入  
        if (buffer.hasRemaining()) {  
            clientChannel.write(buffer, buffer, this);  
        }  
        else {  
            //读取数据  
            ByteBuffer readBuffer = ByteBuffer.allocate(8000);  
            clientChannel.read(readBuffer,readBuffer,new ReadHandler(clientChannel, latch, id, sInfo));  
        }  
    }  
    @Override  
    public void failed(Throwable exc, ByteBuffer attachment) {  
        System.err.println("数据发送失败…");  
        try {  
            clientChannel.close();  
        } catch (IOException e) {  
        } finally {
        	latch.countDown();
        }
    }  
}  