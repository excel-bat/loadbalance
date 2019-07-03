package tools;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * Sigar封装类
 * 
 * @author zhaohr16
 * @date 2019/07/03
 * @FIXME 需配置sigar环境
 */
public class SigarCpu {
    public static double getCpuTotal() throws SigarException {
        double cpuTotal = 0;
        Sigar sigar = new Sigar();
        CpuInfo[] infos = sigar.getCpuInfoList();
        CpuPerc[] cpuList = null;
        cpuList = sigar.getCpuPercList();
        for (int i = 0; i < infos.length; i++) {
            String cpu = CpuPerc.format(cpuList[i].getCombined());
            System.out.println("CPU总占用率: " + cpu);
            cpuTotal += Double.valueOf(cpu);
        }
        return cpuTotal / cpuList.length;
    }

}
