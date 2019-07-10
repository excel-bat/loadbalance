package schedule.strategy;

import monitor.tool.ServerInfo;

/**
 * StrategyRrWithCore class
 * 
 * @author LiWeiqi
 * @date 2019/07/010
 */
public class StrategyRrWithCore implements Strategy {
	public int po = 0;
	public int sCounter = 0;
	private static StrategyRrWithCore strategyRrWithCore;
	private StrategyRrWithCore() {
	}
	public static StrategyRrWithCore getInstance() {
		if (null == strategyRrWithCore) {
			strategyRrWithCore = new StrategyRrWithCore();
			strategyRrWithCore.po = 0;
		}
		return strategyRrWithCore;
	}
	@Override
	public int selectServer(ServerInfo sInfo) {
		int ret = po;
		sCounter = sCounter + 1;
		if (sCounter == sInfo.coreNum[po]) {
			po = po + 1;
			sCounter = 0;
			if (po == sInfo.serverAmount) {
				po = 0;
			}
		}
		return ret;
	}
}
