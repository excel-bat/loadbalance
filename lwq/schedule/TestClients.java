package schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestClients {
	public static RunClient[] clients;
	public static int clientNum;
	public static String stra;
	public static void main(String[] args){
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		try {
			String str;
			System.out.printf("Enter clients amount\n");
            str = stdin.readLine(); 
            clientNum = Integer.parseInt(str);
			System.out.println("Enter Strategy:");
			stra = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		clients = new RunClient[clientNum];
		for (int id = 0; id < clientNum; id++) {
			clients[id] = new RunClient(stra, id);
		}
		for (int id = 0; id < clientNum; id++) {
			new Thread(clients[id]).start();
		}
	}
}
