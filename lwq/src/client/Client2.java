package client;  

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;  

import client.StringConstructor;
import monitor.tool.ServerInfo;  

/**
 * Client class
 * 
 * @author LiWeiqi
 * @date 2019/07/17
 */
public class Client2 {  
    private AsyncClientHandler clientHandle;
    private String server_ip;
    private int server_port;
    private int server_id;
    
    public Client2(int id, String ip, int port) {
    	server_ip = ip;
    	server_port = port;
    	server_id = id;
    	clientHandle = new AsyncClientHandler(ip,port);  
    	String clientName = "Client" + id;
        new Thread(clientHandle,clientName).start();  
    }
    
    /**
     * 向服务器发送消息  
     */
    public boolean sendMsg(int id, byte[] msg, ServerInfo sInfo) throws Exception{  
    	
        clientHandle.sendMsg(msg, id, sInfo);  
        return true;  
    }  
        
    public void sendReq(int id, int status, int len, ServerInfo sInfo) throws UnsupportedEncodingException, Exception {
        if ((status != 0) && (status != 1)) {
        	System.out.println("status error");
        	return;
        }
        if ((len <= 0) || (len > 7992)) {
        	System.out.println("len error");
        	return;
        }
    	sendMsg(id, StringConstructor.constructor(status, len), sInfo);
    }
}  