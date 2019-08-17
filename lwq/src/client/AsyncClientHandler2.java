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
            //�����첽�Ŀͻ���ͨ��  
            clientChannel = AsynchronousSocketChannel.open();  
        } catch (IOException e) { 
        	System.err.println("AsyncClientHandler2 #" + id + "ʧ�ܡ�");  
            e.printStackTrace();  
        }  
      //����CountDownLatch�ȴ�  
        latch = new CountDownLatch(1);  
        //�����첽���Ӳ������ص�������������౾��������ӳɹ���ص�completed����  
        clientChannel.connect(new InetSocketAddress(host, port), this, this); 
    }  
    /**
     * ���ӷ������ɹ�  ��ζ��TCP�����������  
     */
    @Override  
    public void completed(Void result, AsyncClientHandler2 attachment) {  
        //System.out.println("�ͻ��˳ɹ����ӵ���������");  
        //byte[] req = msg.getBytes();  
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);  
        writeBuffer.put(req);  
        writeBuffer.flip();  
        //�첽д  
    	clientChannel.write(writeBuffer, writeBuffer,new WriteHandler(clientChannel, latch, id, sInfo, stime));
    }  
    /**
     * ���ӷ�����ʧ��  
     */
    @Override  
    public void failed(Throwable exc, AsyncClientHandler2 attachment) {  
        System.err.println("���ӷ�����#" + id + "ʧ�ܡ�, IOException.");  
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