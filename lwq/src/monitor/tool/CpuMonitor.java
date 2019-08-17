package monitor.tool;

import monitor.bio.ClientBio;

public class CpuMonitor implements Runnable {
	ServerInfo sInfo;
	int id;
	public CpuMonitor(ServerInfo sInfo, int id) {
		this.sInfo = sInfo;
		this.id = id;
	}
	
	@Override  
    public void run() {  
		while (true) {
			sInfo.renewCpu();
			sInfo.renewLoad();
			System.out.println("----" + id + "-------------------------------");
			sInfo.showCpu();
			sInfo.showLoad();
			sInfo.showAveLoad();
			sInfo.showTp();
			sInfo.showTpsAve();
			sInfo.calcTps();
			sInfo.showTps();
			sInfo.sortCpu();
			sInfo.showErr();
			sInfo.showErrTotal();
			sInfo.showTpComp();
			sInfo.showTpsComp();
			sInfo.clearErr();
			sInfo.showScore1();
			System.out.println("-------------------------------------");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}