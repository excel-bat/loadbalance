package client;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AioClient implements Runnable {
	private static final int MAX_THREAD = 20;
	private int port;
	private String ip;
	private AsynchronousChannelGroup asynChannelGroup;
	private AsynchronousSocketChannel socket;
	
	public AioClient(String ip, int port) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(MAX_THREAD);
		asynChannelGroup = AsynchronousChannelGroup.withThreadPool(executor);
		this.port = port;
		this.ip = ip;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("client start");
			socket = AsynchronousSocketChannel.open(asynChannelGroup);			
			socket.connect(new InetSocketAddress(ip, port), socket, new AioConnectHandler());
			//无效的消耗cpu操作
			while(true) {
				long bac = 1000000;
				bac = bac >> 1;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("finally client");
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		
		
		
		AioClient client = new AioClient("localhost", 9009);
		new Thread(client).start();
		//得到某一进程的cpu占用率，但不能在该进程内使用。
		/*while(true) {
			Thread.sleep(1000);
			System.out.println(CPUMonitorCalc.getInstance().getProcessCpu());
		}*/
		
	}

}
