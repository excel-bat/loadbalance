package schedule.strategy;

import monitor.tool.ServerInfo;


/**
 * StrategyCpuAviMin class
 * 
 * @author LiWeiqi
 * @date 2019/07/09
 */
public class StrategyCpuAviMin implements Strategy {
	//private static StrategyCpuAviMin strategyCpuAviMin;
	public StrategyCpuAviMin() {
	}
	//public static StrategyCpuAviMin getInstance() {
	//	if (null == strategyCpuAviMin) {
	//		strategyCpuAviMin = new StrategyCpuAviMin();
	//	}
	//	return strategyCpuAviMin;
	//}
	@Override
	public int selectServer(ServerInfo sInfo) {
		int ret = 0;
		for (int i = 0; i < sInfo.serverAmount; i++) {
			if (sInfo.serverStatus[i] == 0) {
				ret = i;
				break;
			}
		}		
		
		return ret;
	}
}
