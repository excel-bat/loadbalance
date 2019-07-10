package server;

import java.lang.management.ManagementFactory;
import java.util.Properties;

import com.sun.management.OperatingSystemMXBean;

import tools.SigarInfo;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class StrategyInfo implements Runnable {
    private SigarInfo sigarInfo;
    private static double cpu;
    private static int connectCountTotal;
    private static boolean isWindows;

    public StrategyInfo() {
        sigarInfo = new SigarInfo();
        Properties properties = System.getProperties();
        if (properties.getProperty("os.name").equals("Windows 10")) {
            isWindows = true;
        } else {
            isWindows = false;
        }
        OperatingSystemMXBean osMxBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        cpu = isWindows ? osMxBean.getSystemCpuLoad() : sigarInfo.sigarGetCpuTotal();
        connectCountTotal = 0;
    }

    public static synchronized double getCpu() {
        return cpu;
    }

    private static synchronized void setCpu(double cpu) {
        StrategyInfo.cpu = cpu;
    }

    public static synchronized int getConnectCountTotal() {
        return connectCountTotal;
    }

    public static synchronized void addConnectCountTotal() {
        StrategyInfo.connectCountTotal++;
    }

    @Override
    public void run() {
        while (true) {
            if (isWindows) {
                OperatingSystemMXBean osMxBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
                double cpu = osMxBean.getSystemCpuLoad();
                setCpu(cpu);
            } else {
                setCpu(sigarInfo.sigarGetCpuTotal());
            }

            // Thread.sleep();
        }

    }

}
