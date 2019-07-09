package server;

import monitor.bio.ServerBio;

public class RunServer {
	public static LogMaker mylog = null;
	public static void main(String[] strings) {
		mylog = new LogMaker();
		ServerBio.init();
		Server.run();
		ServerBio.run();
	}
}
