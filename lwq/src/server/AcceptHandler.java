package server;  

import java.nio.ByteBuffer;  
import java.nio.channels.AsynchronousSocketChannel;  
import java.nio.channels.CompletionHandler;  

/**
 * AcceptHandler class
 * 作为handler接收客户端连接
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */ 
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncServerHandler> {  
    @Override  
    public void completed(AsynchronousSocketChannel channel,AsyncServerHandler serverHandler) {  
        //继续接受其他客户端的请求  
        Server.clientCount++;  
        System.out.println("连接的客户端数：" + Server.clientCount);  
        serverHandler.channel.accept(serverHandler, this);  
        //创建新的Buffer  
        ByteBuffer buffer = ByteBuffer.allocate(8000);  
        //异步读  第三个参数为接收消息回调的业务Handler  
        channel.read(buffer, buffer, new ReadHandler(channel));  
    }  
    @Override  
    public void failed(Throwable exc, AsyncServerHandler serverHandler) {  
        exc.printStackTrace();  
        serverHandler.latch.countDown();  
    }  
}  