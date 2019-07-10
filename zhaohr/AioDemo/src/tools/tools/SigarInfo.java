package tools;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class SigarInfo {

    private Sigar sigar;

    public SigarInfo() {
        sigar = new Sigar();
    }

    public double sigarGetCpuTotal() {
        sigar = new Sigar();
        double cpuTotal = 0;
        CpuPerc[] cpuList = null;
        try {
            cpuList = sigar.getCpuPercList();
            for (int i = 0; i < cpuList.length; i++) {
                String cpu = CpuPerc.format(cpuList[i].getCombined());
                if (cpu.contains("N")) {
                    cpu = "100%";
                }
                cpuTotal += Double.valueOf(cpu.substring(0, cpu.length() - 1)) / 100;
            }
            cpuTotal = cpuTotal / cpuList.length;
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return cpuTotal;
    }

}
