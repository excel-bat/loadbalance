package client;  

import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.nio.ByteBuffer;  
import java.nio.channels.AsynchronousSocketChannel;  
import java.nio.channels.CompletionHandler;  
import java.util.concurrent.CountDownLatch;  

/**
 * ReadHandler class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {  
    private AsynchronousSocketChannel clientChannel;  
    private CountDownLatch latch;  
    public ReadHandler(AsynchronousSocketChannel clientChannel,CountDownLatch latch) {  
        this.clientChannel = clientChannel;  
        this.latch = latch;  
    }  
    @Override  
    public void completed(Integer result,ByteBuffer buffer) {  
        buffer.flip();  
        byte[] bytes = new byte[buffer.remaining()];  
        buffer.get(bytes);  
        //String body; 
		//body = new String(bytes,"UTF-8");  
		int len = 0;
		len = (bytes[4] & 0xff) << 24 | 
			  (bytes[5] & 0xff) << 16 | 
			  (bytes[6] & 0xff) << 8  | 
			  (bytes[7] & 0xff);
		String body = ReadResult.show(len, bytes);
		System.out.println("客户端收到结果:"+ body);  
    }  
    @Override  
    public void failed(Throwable exc,ByteBuffer attachment) {  
        System.err.println("数据读取失败…");  
        try {  
            clientChannel.close();  
        } catch (IOException e) {  
        } finally {
        	latch.countDown();
        }
    }  
}  