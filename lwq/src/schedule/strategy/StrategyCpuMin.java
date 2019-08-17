package schedule.strategy;

import monitor.tool.ServerInfo;


/**
 * StrategyCpuMin class
 * 
 * @author LiWeiqi
 * @date 2019/07/09
 */
public class StrategyCpuMin implements Strategy {
	//private static StrategyCpuMin strategyCpuMin;
	public StrategyCpuMin() {
	}
	//public static StrategyCpuMin getInstance() {
	//	if (null == strategyCpuMin) {
	//		strategyCpuMin = new StrategyCpuMin();
	//	}
	//	return strategyCpuMin;
	//}
	@Override
	public int selectServer(ServerInfo sInfo) {
		int ret = sInfo.serverIndex[0];
		return ret;
	}
}
