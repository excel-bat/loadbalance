package client;  

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;  

import client.StringConstructor;  

/**
 * Client class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class Client {  
    private static String DEFAULT_HOST = "127.0.0.1";  
    private static int DEFAULT_PORT = 12345;  
    private static AsyncClientHandler[] clientHandle;
    private static int server_amount = 1;  
    private static String[] server_ip;
    private static int[] server_port;
    public static void start(){  
    	server_ip = new String[server_amount];
    	server_ip[0] = DEFAULT_HOST;
        server_port = new int[server_amount];
        server_port[0] = DEFAULT_PORT;
        clientHandle = new AsyncClientHandler[server_amount];
        start(0, DEFAULT_HOST,DEFAULT_PORT);  
    }  
    public static synchronized void start(int id, String ip,int port){  
        if(clientHandle[id]!=null) {
            return;  
        }
        clientHandle[id] = new AsyncClientHandler(ip,port);  
        String clientName = "Client" + id;
        new Thread(clientHandle[id],clientName).start();  
    }  
    /**
     * 向服务器发送消息  
     */
    public static boolean sendMsg(byte[] msg) throws Exception{  
        clientHandle[0].sendMsg(msg);  
        return true;  
    }  
    public static boolean sendMsg(int id, byte[] msg) throws Exception{  
        clientHandle[id].sendMsg(msg);  
        return true;  
    }  
    @SuppressWarnings("resource")  
    public static void main(String[] args) throws Exception{  
    	BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));     
        String str;
        System.out.println("Enter server amount:");
        str = stdin.readLine(); 
        server_amount = Integer.parseInt(str);            
        server_ip = new String[server_amount];
        server_port = new int[server_amount];
        clientHandle = new AsyncClientHandler[server_amount];
		for (int id = 0; id < server_amount; id++) {
			try {
				System.out.printf("Enter #%d server IP:\n", id);
				str = stdin.readLine(); 
				server_ip[id] = str;
				System.out.printf("Enter #%d server port:\n", id);
				str = stdin.readLine(); 
				server_port[id] = Integer.parseInt(str);
			} catch (IOException e) {
			} 
        }
        
        for (int id = 0; id < server_amount; id++) {
        	Client.start(id, server_ip[id], server_port[id]);
        	System.out.println("Setup at client: " + server_ip[id] + ":" + server_port[id]);
        }
        
        Scanner scanner = new Scanner(System.in);
        int id = 0;
        int status = 0;
        int len = 0;
        while (true) {
        	System.out.println("请输入请求目标：");
        	str = scanner.nextLine(); 
            id = Integer.parseInt(str);
        	System.out.println("请输入请求类型：");
        	str = scanner.nextLine(); 
            status = Integer.parseInt(str);
            System.out.println("请输入请求长度：");
        	str = scanner.nextLine(); 
            len = Integer.parseInt(str);
        	//Client.sendMsg(id, scanner.nextLine().getBytes());
            if ((id < 0) || (id >= server_amount)) {
            	System.out.println("id error");
            	continue;
            }
            if ((status != 0) && (status != 1)) {
            	System.out.println("status error");
            	continue;
            }
            if ((len <= 0) || (len > 7992)) {
            	System.out.println("len error");
            	continue;
            }
        	Client.sendMsg(id, StringConstructor.constructor(status, len));
        }
    }  
}  