package monitor.tool;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * SigarCpuInfo class
 * 
 * @author LiWeiqi
 * @date 2019/07/03
 */
public class SigarCpuInfo {
	public Sigar sigar;
	public SigarCpuInfo() {
    	sigar = new Sigar();
	}
    public void main(String[] args) {
        try {
            System.out.println("get cpu rate: ");
            while (true) {
                Thread.sleep(1000);  
                cpu();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
    }
    public double getCpu() throws SigarException {
        CpuInfo[] infos = sigar.getCpuInfoList();
        CpuPerc[] cpuList = null;
        cpuList = sigar.getCpuPercList();
        double sum = 0.0;
        for (int i = 0; i < infos.length; i++) {
            sum = sum + cpuList[i].getCombined();
        }
        return (sum / infos.length);
    }
    private void cpu() throws SigarException {
        CpuInfo[] infos = sigar.getCpuInfoList();
        CpuPerc[] cpuList = null;
        cpuList = sigar.getCpuPercList();
        double sum = 0.0;
        for (int i = 0; i < infos.length; i++) {
            sum = sum + cpuList[i].getCombined();
        }
        System.out.println(sum / infos.length);
    }
    public int getCoreNum() throws SigarException {
        CpuInfo[] infos = sigar.getCpuInfoList();
        return (infos.length);
    }
    public double[] getBps() throws SigarException, InterruptedException {
    	double[] ret = new double[2];
    	String name = "ens35";
    	
    	long start = System.currentTimeMillis();  
        NetInterfaceStat statStart = sigar.getNetInterfaceStat(name);  
        long rxBytesStart = statStart.getRxBytes();  
        long txBytesStart = statStart.getTxBytes();  
        Thread.sleep(1000);  
        long end = System.currentTimeMillis();  
        NetInterfaceStat statEnd = sigar.getNetInterfaceStat(name);  
        long rxBytesEnd = statEnd.getRxBytes();  
        long txBytesEnd = statEnd.getTxBytes();  
          
        ret[0] = (rxBytesEnd - rxBytesStart)*8/(end-start)*1000;  
        ret[1] = (txBytesEnd - txBytesStart)*8/(end-start)*1000;    

    	return ret;
    }
}