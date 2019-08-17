package schedule.strategy;

import monitor.tool.ServerInfo;

/**
 * Strategy interface
 * 
 * @author LiWeiqi
 * @date 2019/07/08
 */
public interface Strategy {
	/**
	 * Strategy
	 * Interface
	 * @param sInfo information of servers
	 * @return selected server id
	 */
	public int selectServer(ServerInfo sInfo);
}
