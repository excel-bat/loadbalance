package server;

import java.lang.management.ManagementFactory;
import java.util.Properties;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.sun.management.OperatingSystemMXBean;

import tools.SigarInfo;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class StrategyInfo implements Runnable {
    private SigarInfo sigarInfo;
    private static double cpu = 0;
    private static double memory = 0;
    private static long rxBytes = 0;// 流量总数，需要取段处理
    private static double dev = 0;
    private static boolean isWindows;
    private static int connectCountTotal;
    private static int connectCountActive;
    public static int connectAccepted = 0;
    public static int connectFinished = 0;
    public static int connectFailed = 0;

    public StrategyInfo() {
        sigarInfo = new SigarInfo();
        Properties properties = System.getProperties();
        if (properties.getProperty("os.name").equals("Windows 10")) {
            isWindows = true;
        } else {
            isWindows = false;
        }
        connectCountActive = 0;
        connectCountTotal = 0;
        try {
            setInfo();
        } catch (SigarException e) {
            e.printStackTrace();
        }

    }

    public static synchronized double getCpu() {
        return cpu;
    }

    private static synchronized void setCpu(double cpu) {
        StrategyInfo.cpu = cpu;
    }

    public static synchronized double getMemory() {
        return memory;
    }

    private static synchronized void setMemory(double memory) {
        StrategyInfo.memory = memory;
    }

    public static synchronized long getRxBytes() {
        return rxBytes;
    }

    private static synchronized void setRxBytes(long rxBytes) {
        StrategyInfo.rxBytes = rxBytes;
    }

    public static synchronized double getDev() {
        return dev;
    }

    private static synchronized void setDev(double dev) {
        StrategyInfo.dev = dev;
    }

    public static synchronized int getConnectCountTotal() {
        return connectCountTotal;
    }

    public static synchronized void addConnectCountTotal() {
        StrategyInfo.connectCountTotal++;
    }

    public static synchronized int getConnectCountActive() {
        return connectCountActive;
    }

    public static synchronized void setConnectCountActive(int connectCountActive) {
        StrategyInfo.connectCountActive = connectCountActive;
    }

    public void setInfo() throws SigarException {
        if (isWindows) {
            OperatingSystemMXBean osMxBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
            setCpu(osMxBean.getSystemCpuLoad());
        } else {
            Sigar sigar = new Sigar();
            // cpu
            CpuPerc[] cpuList = null;
            double cpu = 0;
            cpuList = sigar.getCpuPercList();
            for (int i = 0; i < cpuList.length; i++) {
                String cpuString = CpuPerc.format(cpuList[i].getCombined());
                if (cpuString.contains("N")) {
                    cpuString = "100%";
                }
                cpu += Double.valueOf(cpuString.substring(0, cpuString.length() - 1)) / 100;
            }
            setCpu(cpu / cpuList.length);
            // memory
            Mem mem = sigar.getMem();
            long memoryFree = mem.getFree();
            long memoryTotal = mem.getTotal();
            double memory = (double)memoryFree / memoryTotal;
            setMemory(memory);
            // rxbytes
            String[] netIfs = sigar.getNetInterfaceList();
            long rxBytes = 0;
            for (String name : netIfs) {
                NetInterfaceStat interfaceStat = sigar.getNetInterfaceStat(name);
                rxBytes += interfaceStat.getRxBytes();
            }
            setRxBytes(rxBytes);
            // dev
            FileSystem[] fsList = sigar.getFileSystemList();
            long devFree = 0;
            long devTotal = 0;
            for (FileSystem fileSystem : fsList) {
                FileSystemUsage usage = sigar.getFileSystemUsage(fileSystem.getDirName());
                if (fileSystem.getType() == 2) {
                    devFree += usage.getFree();
                    devTotal += usage.getTotal();
                }
            }
            double dev = (double)devFree / devTotal;
            setDev(dev);
        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                setInfo();
            } catch (SigarException e) {
                e.printStackTrace();
            }

        }
    }

}
