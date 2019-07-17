package schedule;

import java.util.Random;

import client.Client;
import client.Client2;
import monitor.tool.ServerInfo;
import schedule.strategy.Strategy;
import schedule.strategy.StrategyUtils;

/**
 * RunClient class
 * 
 * @author LiWeiqi
 * @date 2019/07/08
 */
public class RunClient implements Runnable {
	public ServerInfo sInfo;
	public Strategy strategy;
	//public Client client;
	public int threadId;
	
	public RunClient(String stra,int id) {
		sInfo = new ServerInfo();
		//sInfo.init();
		sInfo.init0();
		//client = new Client();
		//client.init(sInfo);
		sInfo.getCore();
		strategy = StrategyUtils.getStrategy(stra);
		if (strategy == null) {
			System.out.println("Strategy name error");
			return;
		}
		threadId = id;
	}
	
	@Override
	public void run() {
		while (true) {
			sInfo.renewCpu();
			sInfo.sortCpu();
			sInfo.showCpu();
			int victim = strategy.selectServer(sInfo);
			System.out.println("  Selected server is #" + victim);
			
			Random rand = new Random();
	        int status = rand.nextInt(2);
	        int len = rand.nextInt(7992) + 1;
	        
	        Client2 client = new Client2(victim, sInfo.serverIp[victim], sInfo.serverPort[victim]);
	        System.out.println("  sending to #" + victim + " status:" + status + " len:" + len);
	        try {
				client.sendReq(victim, status, len, sInfo);
				//Thread.sleep(1);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        /*
			try {
				System.out.println("  waiting for #" + victim);
				while (sInfo.serverStatus[victim] == 1) {Thread.sleep(1);};
				sInfo.startTime[victim] = System.currentTimeMillis();
				System.out.println("  sending to #" + victim + " status:" + status + " len:" + len);
				sInfo.serverStatus[victim] = 1;
				client.sendReq(victim, status, len, sInfo);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        */
		}
	}
}
