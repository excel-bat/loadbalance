package monitor.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import server.RunServer;

/**
 * ServerBio class
 * 
 * @author LiWeiqi
 * @date 2019/07/02
 */
public class ServerBio{
    private static ServerSocket server;
    private static int server_port;
    public static void init() {
    	if(server != null) {
    		System.out.println("Monitor is already running...");
        	return;
        }
       	String str = "";
       	BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));      
       	System.out.println("Enter server port for monitor:");
		try {
			str = stdin.readLine();
			server_port = Integer.parseInt(str);
			server = new ServerSocket(server_port);
			System.out.println("Setup at port for monitor: " + server_port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static void run() {
    	new Thread(new ServerBioThread(server)).start();
    }
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
            RunServer.mylog.logServer("Setup monitor at port: " + server_port);
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
