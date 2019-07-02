package server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel; 
import java.nio.channels.AsynchronousSocketChannel; 
import java.nio.channels.CompletionHandler;

public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
	private static final int BUFFER_SIZE = 1024;
	
	@Override
	public void completed(AsynchronousSocketChannel socket, AsynchronousServerSocketChannel serverSocket) {
		try {
			//递归保持监听
			serverSocket.accept(serverSocket, this);
			System.out.println("connected from： "+socket.getRemoteAddress().toString());
			//读并处理
			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
			buffer.clear();
			socket.read(buffer, socket, new AioReadHandler(buffer));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
		exc.printStackTrace();
		
	}
	

}
