package client;  

import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.nio.ByteBuffer;  
import java.nio.channels.AsynchronousSocketChannel;  
import java.nio.channels.CompletionHandler;  
import java.util.concurrent.CountDownLatch;

import monitor.tool.ServerInfo;
import schedule.TestSchedule;  

/**
 * ReadHandler class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {  
    private AsynchronousSocketChannel clientChannel;  
    private CountDownLatch latch;  
    private int id;
    private long stime;
    private ServerInfo sInfo;
    public ReadHandler(AsynchronousSocketChannel clientChannel,CountDownLatch latch,int id,ServerInfo sInfo, long stime) {  
        this.clientChannel = clientChannel;  
        this.latch = latch;  
        this.id = id;
        this.sInfo = sInfo;
        this.stime = stime;
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
		//System.out.println("客户端收到结果:"+ body);
		//System.out.println("客户端收到结果");
		sInfo.serverStatus[id] = 0;
		sInfo.endTime[id] = System.currentTimeMillis();
		sInfo.useTime[id] = (sInfo.endTime[id] - stime);
		sInfo.lasTime[id] = sInfo.aveTime[id];
		sInfo.aveTime[id] = 0.2 * sInfo.aveTime[id] + 0.8 * sInfo.useTime[id];
		//System.out.println("客户端#" + id + "收到结果，用时 " + sInfo.useTime[id]);
		//System.out.println("客户端收到结果，用时2 " + (sInfo.endTime[id] - stime));
		try {
			clientChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
    @Override  
    public void failed(Throwable exc,ByteBuffer attachment) {  
        System.err.println("数据读取失败…");  
        sInfo.errTemp[id]++;
        sInfo.errTotal[id]++;
        try {  
            clientChannel.close();  
        } catch (IOException e) {  
        } finally {
        	latch.countDown();
        }
    }  
}  