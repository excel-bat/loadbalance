package test;

import java.util.Scanner;  
import client.Client;  
import server.Server;  
/** 
 * ���Է��� 
 * @author yangtao__anxpp.com 
 * @version 1.0 
 */  
public class TestAio {  
    /**
     * ����������  
     * @param args
     * @throws Exception
     */
    @SuppressWarnings("resource")  
    public static void main(String[] args) throws Exception{  
        //���з�����  
        Server.start();  
        //����ͻ������ڷ���������ǰִ�д���  
        Thread.sleep(100);  
        //���пͻ���   
        Client.start();  
        System.out.println("������������Ϣ��");  
        Scanner scanner = new Scanner(System.in);  
        while(Client.sendMsg(scanner.nextLine().getBytes())) {};  
    }  
}  