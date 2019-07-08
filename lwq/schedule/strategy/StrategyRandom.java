package schedule.strategy;

import java.util.Random;

import monitor.tool.ServerInfo;

public class StrategyRandom implements Strategy {
	private static StrategyRandom strategyRandom;
	private StrategyRandom() {
	}
	public static StrategyRandom getInstance() {
		if (null == strategyRandom) {
			strategyRandom = new StrategyRandom();
		}
		return strategyRandom;
	}
	@Override
	public int selectServer(ServerInfo sInfo) {
		int n = sInfo.serverAmount;
		Random rand=new Random();
        return rand.nextInt(n); 
	}
}
