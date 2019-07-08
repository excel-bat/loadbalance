package schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import client.Client;
import monitor.tool.ServerInfo;
import schedule.strategy.Strategy;
import schedule.strategy.StrategyUtils;

public class TestSchedule {
	private static ServerInfo sInfo;
	private static Strategy strategy;
	private static Client client;
	public static void main(String[] args){  	
		sInfo = new ServerInfo();
		sInfo.init();
		client = new Client();
		client.init(sInfo);
		String str = "";
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter Strategy:");
		try {
			str = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		strategy = StrategyUtils.getStrategy(str);
		if (strategy == null) {
			System.out.println("Strategy name error");
			return;
		}
		while (true) {
			sInfo.renewCpu();
			sInfo.showCpu();
			sInfo.sortCpu();
			int victim = strategy.selectServer(sInfo);
			System.out.println("  Selected server is #" + victim);
			
			int n = sInfo.serverAmount;
			Random rand = new Random();
	        int status = rand.nextInt(2);
	        int len = rand.nextInt(7992) + 1;
			try {
				System.out.println("  sending to #" + victim + " status:" + status + " len:" + len);
				client.sendReq(victim, status, len);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
            System.out.println("Enter to continue:");
			try {
				str = stdin.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
}
