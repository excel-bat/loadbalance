package server;  

import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.nio.ByteBuffer;  
import java.nio.channels.AsynchronousSocketChannel;  
import java.nio.channels.CompletionHandler;  

/**
 * ReadHandler class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {  
    /**
     * 用于读取半包消息和发送应答  
     */
    private AsynchronousSocketChannel channel;  
    
    public ReadHandler(AsynchronousSocketChannel channel) {  
            this.channel = channel;  
    }  
    /**
     * 读取到消息后的处理  
     */
    @Override  
    public void completed(Integer result, ByteBuffer attachment) {  
        //flip操作  
        attachment.flip();  
        //根据  
        byte[] message = new byte[attachment.remaining()];  
        attachment.get(message);  
        try {  
            //String expression = new String(message, "UTF-8");
        	int[] req = StringDecoder.decoder(message);
        	if ((req[1] == 0) && (req[0] == 0)) {
        		return;
        	}
            System.out.println("服务器收到消息: " + req[0] + " " + req[1]);  
            RunServer.mylog.logServer("服务器收到消息: " + req[0] + " " + req[1]);
            Server.counter.addd();
            
            if (req[0] == 0) {
            	System.out.println("searching in file");
            	byte[] calrResult = Result.ask(req[1]);
            	System.out.println("searching done");
                doWrite(calrResult);  
                RunServer.mylog.logServer("服务器发送回复");
            } else {
            	doWrite(Result.getWrit());       
            	RunServer.mylog.logServer("服务器发送回复");
            	Result.write(req[1], message);     	
            }
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
    }  
    /**
     * 发送消息  
     * @param result
     */
    private void doWrite(byte[] bytes) {  
        //byte[] bytes = result.getBytes();  
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);  
        writeBuffer.put(bytes);  
        writeBuffer.flip();  
        //异步写数据 参数与前面的read一样  
        channel.write(writeBuffer, writeBuffer,new CompletionHandler<Integer, ByteBuffer>() {  
            @Override  
            public void completed(Integer result, ByteBuffer buffer) {  
                //如果没有发送完，就继续发送直到完成  
                if (buffer.hasRemaining()) {  
                    channel.write(buffer, buffer, this);
                    try {
						channel.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                }else{
                	try {
						channel.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
                    //创建新的Buffer  
                    ByteBuffer readBuffer = ByteBuffer.allocate(8000);  
                    //异步读  第三个参数为接收消息回调的业务Handler  
                    channel.read(readBuffer, readBuffer, new ReadHandler(channel));  
                }  
            }  
            @Override  
            public void failed(Throwable exc, ByteBuffer attachment) {  
                try {  
                    channel.close();  
                } catch (IOException e) {  
                }  
            }  
        });  
    }  
    @Override  
    public void failed(Throwable exc, ByteBuffer attachment) {  
        try {  
            this.channel.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}  