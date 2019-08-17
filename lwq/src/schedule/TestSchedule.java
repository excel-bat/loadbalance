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

/**
 * TestSchedule class
 * 
 * @author LiWeiqi
 * @date 2019/07/08
 */
public class TestSchedule {
	public static ServerInfo sInfo;
	private static Strategy strategy;
	private static Client client;
	public static void main(String[] args){  	
		sInfo = new ServerInfo();
		//sInfo.init();
		sInfo.init0();
		client = new Client();
		client.init(sInfo);
		
		sInfo.getCore();
		for (int i = 0; i < sInfo.serverAmount; i++) {
			System.out.println("" + i + "# has " + sInfo.coreNum[i] + " core");
		}
		
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
			sInfo.sortCpu();
			sInfo.showCpu();
			int victim = strategy.selectServer(sInfo);
			System.out.println("  Selected server is #" + victim);
			
			int n = sInfo.serverAmount;
			Random rand = new Random();
	        int status = rand.nextInt(2);
	        int len = rand.nextInt(7992) + 1;
			try {
				System.out.println("  waiting for #" + victim);
				while (sInfo.serverStatus[victim] == 1) {Thread.sleep(1);};
				System.out.println("  sending to #" + victim + " status:" + status + " len:" + len);
				sInfo.serverStatus[victim] = 1;
				//client.sendReq(victim, status, len,sInfo);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			/*
            System.out.println("Enter to continue:");
            
			try {
				str = stdin.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			*/
		}
	}
}
