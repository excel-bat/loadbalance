package server;

import monitor.bio.ServerBio;

/**
 * RunServer class
 * 
 * @author LiWeiqi
 * @date 2019/07/05
 */
public class RunServer {
	public static LogMaker mylog = null;
	public static void main(String[] strings) {
		mylog = new LogMaker();
		ServerBio.init();
		Server.run();
		ServerBio.run();
	}
}
