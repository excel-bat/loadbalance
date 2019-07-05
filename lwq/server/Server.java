package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Server class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class Server {  
    private static int DEFAULT_PORT = 12345;  
    private static AsyncServerHandler serverHandle;  
    public volatile static long clientCount = 0;  
    public volatile static Counter counter;
    public static void start(){  
        start(DEFAULT_PORT);  
    }  
    public static synchronized void start(int port){  
        if(serverHandle!=null) { 
            return;  
        }
        serverHandle = new AsyncServerHandler(port);  
        new Thread(serverHandle,"Server").start();  
        counter = new Counter(3);
        new Thread(counter, "Counter").start();
    }  
    public static void main(String[] args){
    	BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));      
    	System.out.println("Enter server port:");
    	int serverPort;
		String str;
		try {
			str = stdin.readLine();
			serverPort = Integer.parseInt(str);
		} catch (IOException e) {
			System.out.println("Setup at port input Error!");
			serverPort = DEFAULT_PORT;
		} 
        System.out.println("Setup at port: " + serverPort);
        Server.start(serverPort);  
    }  
}  