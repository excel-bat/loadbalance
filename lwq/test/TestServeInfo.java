package test;

import monitor.tool.ServerInfo;

public class TestServeInfo {
	public static void main(String[] args) {
		ServerInfo sInfo = new ServerInfo();
		sInfo.init();
		sInfo.renewCpu();
		sInfo.sortCpu();
		sInfo.showCpu();
	}
}
