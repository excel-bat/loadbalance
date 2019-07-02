package tools;


import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.CpuInfo;

public class SigarCpu {
	public static double getCpuTotal() throws SigarException {
		double cpuTotal = 0;		
		Sigar sigar = new Sigar();
		CpuInfo[] infos = sigar.getCpuInfoList();
		CpuPerc cpuList[] = null;
		cpuList = sigar.getCpuPercList();
		for (int i = 0; i < infos.length; i++) {
			String cpu = CpuPerc.format(cpuList[i].getCombined());
			System.out.println("CPU总使用率: "+cpu);
			cpuTotal += Double.valueOf(cpu);
		}
		return cpuTotal/cpuList.length;
	}

}
