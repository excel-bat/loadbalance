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
     * ���ڶ�ȡ�����Ϣ�ͷ���Ӧ��  
     */
    private AsynchronousSocketChannel channel;  
    
    public ReadHandler(AsynchronousSocketChannel channel) {  
            this.channel = channel;  
    }  
    /**
     * ��ȡ����Ϣ��Ĵ���  
     */
    @Override  
    public void completed(Integer result, ByteBuffer attachment) {  
        //flip����  
        attachment.flip();  
        //����  
        byte[] message = new byte[attachment.remaining()];  
        attachment.get(message);  
        try {  
            //String expression = new String(message, "UTF-8");
        	int[] req = StringDecoder.decoder(message);
            System.out.println("�������յ���Ϣ: " + req[0] + " " + req[1]);  
            Server.counter.addd();
            
            if (req[0] == 0) {
            	byte[] calrResult = Result.ask(req[1]);
                doWrite(calrResult);  
            } else {
            	Result.write(req[1], message);
            	doWrite(Result.getWrit());            	
            }
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
    }  
    /**
     * ������Ϣ  
     * @param result
     */
    private void doWrite(byte[] bytes) {  
        //byte[] bytes = result.getBytes();  
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);  
        writeBuffer.put(bytes);  
        writeBuffer.flip();  
        //�첽д���� ������ǰ���readһ��  
        channel.write(writeBuffer, writeBuffer,new CompletionHandler<Integer, ByteBuffer>() {  
            @Override  
            public void completed(Integer result, ByteBuffer buffer) {  
                //���û�з����꣬�ͼ�������ֱ�����  
                if (buffer.hasRemaining())  
                    channel.write(buffer, buffer, this);  
                else{  
                    //�����µ�Buffer  
                    ByteBuffer readBuffer = ByteBuffer.allocate(8000);  
                    //�첽��  ����������Ϊ������Ϣ�ص���ҵ��Handler  
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