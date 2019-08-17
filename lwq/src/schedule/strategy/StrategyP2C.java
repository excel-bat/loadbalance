package schedule.strategy;

import java.util.Random;

import monitor.tool.ServerInfo;

/**
 * StrategyP2C class
 * 
 * @author LiWeiqi
 * @date 2019/08/08
 */
public class StrategyP2C implements Strategy {
	//private static StrategyP2C strategyP2C;
	public StrategyP2C() {
	}
	//public static StrategyP2C getInstance() {
	//	if (null == strategyP2C) {
	//		strategyP2C = new StrategyP2C();
	//	}
	//	return strategyP2C;
	//}
	@Override
	public int selectServer(ServerInfo sInfo) {
		int n = sInfo.serverAmount;
		Random rand=new Random();
		int a = rand.nextInt(n);
		int b = rand.nextInt(n);
		while (a == b) {
			b = rand.nextInt(n);
		}
		if (sInfo.useTime[a] < sInfo.useTime[b]) {
			return a;
		} else {
			return b;
		} 
	}
}
