package schedule.strategy;

import java.util.Random;

import monitor.tool.ServerInfo;

/**
 * StrategyPickKX class
 * 
 * @author LiWeiqi
 * @date 2019/08/12
 */
public class StrategyPickKXAve implements Strategy {
	//private static StrategyPickKX strategyPickKX;
	public StrategyPickKXAve() {
	}
	//public static StrategyPickKX getInstance() {
	//	if (null == strategyPickKX) {
	//		strategyPickKX = new StrategyPickKX();
	//	}
	//	return strategyPickKX;
	//}
	@Override
	public int selectServer(ServerInfo sInfo) {
		double lTotal = 0.0;
		for (int id = 0; id < sInfo.serverAmount; id++) {
			lTotal = lTotal + sInfo.aveLoad[id];
		}
		double xSum = 0.0;
		for (int id = 0; id < sInfo.serverAmount; id++) {
			sInfo.score1[id] = (lTotal - sInfo.aveLoad[id]) / (lTotal);
			xSum = xSum + sInfo.score1[id];
		}
		for (int id = 0; id < sInfo.serverAmount; id++) {
			sInfo.score1[id] = (sInfo.score1[id]) / (xSum);
		}
		Random randomno = new Random();
		double t = randomno.nextDouble();
		double ts = 0.0;
		//for (int id = 0; id < sInfo.serverAmount; id++) {
		//	System.out.print(sInfo.score1[id] + ";");
		//}
		//System.out.print(t + ";");
		for (int id = 0; id < sInfo.serverAmount; id++) {
			if ((t >= ts) && (t < ts + sInfo.score1[id])) {
				//System.out.println(id);
				return id;
			}
			ts = ts + sInfo.score1[id];
		}
		return randomno.nextInt(sInfo.serverAmount); 
	}
}
