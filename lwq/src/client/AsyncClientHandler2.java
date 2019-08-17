package client;  

import java.io.IOException;  
import java.net.InetSocketAddress;  
import java.nio.ByteBuffer;  
import java.nio.channels.AsynchronousSocketChannel;  
import java.nio.channels.CompletionHandler;  
import java.util.concurrent.CountDownLatch;

import monitor.tool.ServerInfo;  

/**
 * AsyncClientHandler class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class AsyncClientHandler2 implements CompletionHandler<Void, AsyncClientHandler2> {  
    private AsynchronousSocketChannel clientChannel;  
    private String host;  
    private int port;  
    private CountDownLatch latch;  
    byte[] req;
    int id;
    private long stime;
    ServerInfo sInfo;
    public AsyncClientHandler2(String host, int port,byte[] req, int id, ServerInfo sInfo, long stime) {  
        this.host = host;  
        this.port = port;  
        this.req = req;
        this.id = id;
        this.sInfo = sInfo;
        this.stime = stime;
        try {  
            //创建异步的客户端通道  
            clientChannel = AsynchronousSocketChannel.open();  
        } catch (IOException e) { 
        	System.err.println("AsyncClientHandler2 #" + id + "失败…");  
            e.printStackTrace();  
        }  
      //创建CountDownLatch等待  
        latch = new CountDownLatch(1);  
        //发起异步连接操作，回调参数就是这个类本身，如果连接成功会回调completed方法  
        clientChannel.connect(new InetSocketAddress(host, port), this, this); 
    }  
    /**
     * 连接服务器成功  意味着TCP三次握手完成  
     */
    @Override  
    public void completed(Void result, AsyncClientHandler2 attachment) {  
        //System.out.println("客户端成功连接到服务器…");  
        //byte[] req = msg.getBytes();  
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);  
        writeBuffer.put(req);  
        writeBuffer.flip();  
        //异步写  
    	clientChannel.write(writeBuffer, writeBuffer,new WriteHandler(clientChannel, latch, id, sInfo, stime));
    }  
    /**
     * 连接服务器失败  
     */
    @Override  
    public void failed(Throwable exc, AsyncClientHandler2 attachment) {  
        System.err.println("连接服务器#" + id + "失败…, IOException.");  
        sInfo.errTemp[id]++;
        sInfo.errTotal[id]++;
        //exc.printStackTrace();  
        try {  
            clientChannel.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {
        	latch.countDown();
        }
    }  
}  