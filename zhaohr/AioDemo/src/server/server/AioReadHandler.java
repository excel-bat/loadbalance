package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class AioReadHandler implements CompletionHandler<Integer, AsynchronousSocketChannel>{
	private ByteBuffer buffer;
	private CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
	
	public AioReadHandler(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public void completed(Integer i, AsynchronousSocketChannel socket) {
		try {
			if (i > 0) {
				buffer.flip();
				String msg = decoder.decode(buffer).toString();
				System.out.println("server read from "+socket.getRemoteAddress()+"     message: "+msg);
				//������һ���첽��
				//socket.read(buffer, socket, this);
				//��������
			}
			else if (i == -1) {
				System.out.println("read wrong!");
				buffer = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
		exc.printStackTrace();
		
	}

}
