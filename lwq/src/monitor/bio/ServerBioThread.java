package monitor.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ServerBioThread class
 * 
 * @author LiWeiqi
 * @date 2019/07/08
 */
public class ServerBioThread implements Runnable {
	public static  ServerSocket server;
	public ServerBioThread(ServerSocket server) {
        this.server = server;
    }
	@Override
	public void run() {
		try {
			while(true){
				Socket socket = null;
				try {
					socket = server.accept();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("monitor server accept failed");
				}
				new Thread(new ServerHandler(socket)).start();
			}
		}finally{
            if(server != null){
                System.out.println("Closed");
                try {
					server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                server = null;
            }
        }
	}

}
