package monitor.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBioThread implements Runnable {
	public static  ServerSocket server;
	public ServerBioThread(ServerSocket server) {
        this.server = server;
    }
	public void run() {
		try {
			while(true){
				Socket socket = null;
				try {
					socket = server.accept();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
