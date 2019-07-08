package schedule.strategy;

import monitor.tool.ServerInfo;

public interface Strategy {
	public int selectServer(ServerInfo sInfo);
}
