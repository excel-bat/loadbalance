package server;

import java.io.IOException;

import monitor.bio.ServerBio;

public class RunServer {
	public static void main(String[] strings) {
		try {
			ServerBio.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Server.run();
	}
}
