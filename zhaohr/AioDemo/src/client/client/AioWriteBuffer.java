package client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AioWriteBuffer implements CompletionHandler<Integer, AsynchronousSocketChannel>{
	private ByteBuffer buffer;
	public AioWriteBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void completed(Integer i, AsynchronousSocketChannel socket) {
		try {
			if (i > 0) {
				System.out.println("client sending message");
				//socket.write(buffer, socket, this);
			}
			else if (i == -1) {
				System.out.print("client write wrong");
				buffer = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
		exc.printStackTrace();		
	}

}
