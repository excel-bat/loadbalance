package test;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import monitor.tool.*;

public class TestBps {
	public static void main(String[] args) throws SigarException, InterruptedException {
		Sigar sigar = new Sigar();
		String[] netIfs = sigar.getNetInterfaceList(); 
		for (int i = 0; i < netIfs.length; i++) {
			System.out.println(netIfs[i]);
		}
			
		while (true) {
			SigarCpuInfo sinfo = new SigarCpuInfo();
			double[] ret = sinfo.getBps();
			System.out.println(ret[0]);
			System.out.println(ret[1]);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}