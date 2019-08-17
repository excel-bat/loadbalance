package schedule.strategy;

import java.util.Random;

import monitor.tool.ServerInfo;

/**
 * StrategyPickKX class
 * 
 * @author LiWeiqi
 * @date 2019/08/12
 */
public class StrategyMy implements Strategy {
	//private static StrategyPickKX strategyPickKX;
	public StrategyMy() {
	}
	//public static StrategyPickKX getInstance() {
	//	if (null == strategyPickKX) {
	//		strategyPickKX = new StrategyPickKX();
	//	}
	//	return strategyPickKX;
	//}
	@Override
	public int selectServer(ServerInfo sInfo) {
		double maxcpu = 0.0;
		double maxcore = 0.0;
		double maxload = 0.0;
		double maxaveload = 0.0;
		double maxtime = 0.0;
		double ccpu = 20;
		double ccore = 20;
		double cload = 80;
		double caveload = 40;
		double ctime = 100;
		for (int id = 0; id < sInfo.serverAmount; id++) {
			if (1.0 * sInfo.err[id] / sInfo.tps[id] > 0.2) {
				sInfo.score2[id] = 0;
			}
			if ((1.0 * sInfo.err[id] / sInfo.tps[id] <= 0.2) && (1.0 * sInfo.err[id] / sInfo.tps[id] > 0.1)) {
				sInfo.score2[id] = 0.25;
			}
			if ((1.0 * sInfo.err[id] / sInfo.tps[id] <= 0.1) && (1.0 * sInfo.err[id] / sInfo.tps[id] > 0)) {
				sInfo.score2[id] = 0.5;
			}
			if (1.0 * sInfo.err[id] / sInfo.tps[id] <= 0) {
				sInfo.score2[id] = 1;
			}
			if (sInfo.coreNum[id] > maxcore) { maxcore = sInfo.coreNum[id];}
			if (sInfo.serverCpuRate[id] > maxcpu) { maxcpu = sInfo.serverCpuRate[id];}
			if (sInfo.loads[id] > maxload) { maxload = sInfo.loads[id];}
			if (sInfo.aveLoad[id] > maxaveload) { maxaveload = sInfo.aveLoad[id];}
			if (sInfo.useTime[id] > maxtime) { maxtime = sInfo.useTime[id];}
		}
		for (int id = 0; id < sInfo.serverAmount; id++) {
			sInfo.score3[id] = ((ccore * sInfo.coreNum[id] / maxcore) + 
					            (ccpu * (1 - sInfo.serverCpuRate[id] / maxcpu)) + 
					            (cload * (1 - sInfo.loads[id] / maxload)) + 
					            (caveload * (1 - sInfo.aveLoad[id] / maxaveload)) + 
					            (ctime * (1 - sInfo.useTime[id] / maxtime))) * sInfo.score2[id];
		}
		int a1;
		int b1;
		if (sInfo.score3[0] > sInfo.score3[1]) {
			a1 = 0;
			b1 = 1;
		} else {
			a1 = 1;
			b1 = 0;
		}
		for (int id = 2; id < sInfo.serverAmount; id++) {
			if (sInfo.score3[id] > sInfo.score3[a1]) {
				b1 = a1;
				a1 = id;
				continue;
			}
			if (sInfo.score3[id] > sInfo.score3[b1]) {
				b1 = id;
				continue;
			}
		}
		Random randomno = new Random();
		double t = randomno.nextDouble();
		if (t < 0.7) {
			return a1;
		} else {
			return b1;
		}
	}
}
