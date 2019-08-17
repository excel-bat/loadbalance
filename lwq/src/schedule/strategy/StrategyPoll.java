package schedule.strategy;

import monitor.tool.ServerInfo;

/**
 * StrategyPoll class
 * 
 * @author LiWeiqi
 * @date 2019/07/09
 */
public class StrategyPoll implements Strategy {
	public int po = 0;
	//private static StrategyPoll strategyPoll;
	public StrategyPoll() {
		po = 0;
	}
	//public static StrategyPoll getInstance() {
		//if (null == strategyPoll) {
		//	strategyPoll = new StrategyPoll();
		//	strategyPoll.po = 0;
		//}
		//return strategyPoll;
	//}
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
