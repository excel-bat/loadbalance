package monitor.tool;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * SigarCpuInfo class
 * 
 * @author LiWeiqi
 * @date 2019/07/03
 */
public class SigarCpuInfo {
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
    	Sigar sigar = new Sigar();
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
        Sigar sigar = new Sigar();
        CpuInfo[] infos = sigar.getCpuInfoList();
        CpuPerc[] cpuList = null;
        cpuList = sigar.getCpuPercList();
        double sum = 0.0;
        for (int i = 0; i < infos.length; i++) {
            sum = sum + cpuList[i].getCombined();
        }
        System.out.println(sum / infos.length);
    }

}