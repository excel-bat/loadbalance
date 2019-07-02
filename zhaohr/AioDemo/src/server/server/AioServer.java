package server;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup; 
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AioServer implements Runnable{
	private static final int MAX_THREAD = 20;
	private AsynchronousChannelGroup asynChannelGroup;
	private AsynchronousServerSocketChannel serverSocket;
	
	public AioServer(int port) throws Exception {
		//用线程池创建异步通道管理器Group再创建serverSocket
		ExecutorService executor = Executors.newFixedThreadPool(MAX_THREAD);
		asynChannelGroup = AsynchronousChannelGroup.withThreadPool(executor);
		serverSocket = AsynchronousServerSocketChannel.open(asynChannelGroup).bind(new InetSocketAddress(port));
	}
	
	public void run() {
		System.out.println("server start");
		try {
			serverSocket.accept(serverSocket, new AioAcceptHandler());
			Thread.sleep(400000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("finally server");
		}
	}

	public static void main(String[] args) throws Exception {
		AioServer server = new AioServer(9009);
		new Thread(server).start();
		
	}

}
