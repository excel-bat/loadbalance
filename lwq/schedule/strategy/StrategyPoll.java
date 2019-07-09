package schedule.strategy;

import monitor.tool.ServerInfo;

public class StrategyPoll implements Strategy {
	public int po = 0;
	private static StrategyPoll strategyPoll;
	private StrategyPoll() {
	}
	public static StrategyPoll getInstance() {
		if (null == strategyPoll) {
			strategyPoll = new StrategyPoll();
			strategyPoll.po = 0;
		}
		return strategyPoll;
	}
	@Override
	public int selectServer(ServerInfo sInfo) {
		int ret = po;
		po = po + 1;
		if (po == sInfo.serverAmount) {
			po = 0;
		}
		return ret;
	}
}
