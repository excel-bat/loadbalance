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
public class AsyncClientHandler implements CompletionHandler<Void, AsyncClientHandler>, Runnable {  
    private AsynchronousSocketChannel clientChannel;  
    private String host;  
    private int port;  
    private CountDownLatch latch;  
    public AsyncClientHandler(String host, int port) {  
        this.host = host;  
        this.port = port;  
        try {  
            //�����첽�Ŀͻ���ͨ��  
            clientChannel = AsynchronousSocketChannel.open();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    @Override  
    public void run() {  
        //����CountDownLatch�ȴ�  
        latch = new CountDownLatch(1);  
        //�����첽���Ӳ������ص�������������౾��������ӳɹ���ص�completed����  
        clientChannel.connect(new InetSocketAddress(host, port), this, this);  
        try {  
            latch.await();  
        } catch (InterruptedException e1) {  
            e1.printStackTrace();  
        }  
        try {  
            clientChannel.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    /**
     * ���ӷ������ɹ�  ��ζ��TCP�����������  
     */
    @Override  
    public void completed(Void result, AsyncClientHandler attachment) {  
        System.out.println("�ͻ��˳ɹ����ӵ���������");  
    }  
    /**
     * ���ӷ�����ʧ��  
     */
    @Override  
    public void failed(Throwable exc, AsyncClientHandler attachment) {  
        System.err.println("���ӷ�����ʧ�ܡ�");  
        exc.printStackTrace();  
        try {  
            clientChannel.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {
        	latch.countDown();
        }
    }  
    /**
     * �������������Ϣ  
     * @param msg
     */
    public void sendMsg(byte[] req, int id, ServerInfo sInfo){  
        //byte[] req = msg.getBytes();  
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);  
        writeBuffer.put(req);  
        writeBuffer.flip();  
        //�첽д  
        clientChannel.write(writeBuffer, writeBuffer,new WriteHandler(clientChannel, latch, id, sInfo));  
    }  
}  