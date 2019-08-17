package schedule;

import java.util.Random;

import client.Client;
import client.Client2;
import monitor.tool.ServerInfo;
import schedule.strategy.Strategy;
import schedule.strategy.StrategyUtils;
import speed.Speed;
import speed.SpeedUtils;

/**
 * RunClient class
 * 
 * @author LiWeiqi
 * @date 2019/07/08
 */
public class RunClient implements Runnable {
	public ServerInfo sInfo;
	public Strategy strategy;
	public Speed speed;
	public Client client;
	public int threadId;
	
	public RunClient(String stra,int id) {
		sInfo = new ServerInfo();
		//sInfo.init();
		sInfo.init0();
		
		sInfo.getCore();
		sInfo.runMonitor(id);
		strategy = StrategyUtils.getStrategy(stra);
		if (strategy == null) {
			System.out.println("Strategy name error");
			return;
		}
		threadId = id;
		speed = SpeedUtils.getSpeed("random");
	}
	
	@Override
	public void run() {
		Random rand = new Random();
		int victim;
		int status;
        int len;
        sInfo.sttime = System.currentTimeMillis();
		while (true) {
			//sInfo.showCpu();
			victim = 0;
			try {
				victim = strategy.selectServer(sInfo);
				sInfo.tp[victim] = sInfo.tp[victim] + 1;
			} catch (Exception e) {
				
			}
			//System.out.println("  Selected server is #" + victim);
			
	        status = rand.nextInt(2);
	        len = rand.nextInt(7992) + 1;
	        //System.out.println("  sending to #" + victim + " status:" + status + " len:" + len);
	        if (false) {
	        	Client2 client = new Client2(victim, sInfo.serverIp[victim], sInfo.serverPort[victim]);
	        } else {
	        	try {
		        	client = new Client();
		        	client.init(sInfo);
		        	sInfo.startTime[victim] = System.currentTimeMillis();
					client.sendReq(victim, status, len, sInfo, System.currentTimeMillis());
					int waittime = speed.getSleep();
					//System.out.println("wait time = " + waittime);
					Thread.sleep(waittime);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
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
