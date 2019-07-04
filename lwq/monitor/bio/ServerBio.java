package monitor.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * ServerBio class
 * 
 * @author LiWeiqi
 * @date 2019/07/02
 */
public class ServerBio {
    private static ServerSocket server;
    private static int server_port;
    public static void main(String[] strings) throws IOException{
        if(server != null) {
        	return;
        }
        try{
        	String str;
        	BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));      
        	System.out.println("Enter server port:");
			str = stdin.readLine(); 
			server_port = Integer.parseInt(str);
            server = new ServerSocket(server_port);
            System.out.println("Setup at port: " + server_port);
            while(true){
                Socket socket = server.accept();
                new Thread(new ServerHandler(socket)).start();
            }
        }finally{
            if(server != null){
                System.out.println("Closed");
                server.close();
                server = null;
            }
        }
    }
}
