package schedule.strategy;

import monitor.tool.ServerInfo;

public class StrategyCpuMin implements Strategy {
	private static StrategyCpuMin strategyCpuMin;
	private StrategyCpuMin() {
	}
	public static StrategyCpuMin getInstance() {
		if (null == strategyCpuMin) {
			strategyCpuMin = new StrategyCpuMin();
		}
		return strategyCpuMin;
	}
	@Override
	public int selectServer(ServerInfo sInfo) {
		int ret = sInfo.serverIndex[0];
		return ret;
	}
}
