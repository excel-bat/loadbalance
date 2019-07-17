package server;

import java.lang.management.ManagementFactory;
import java.util.Properties;

import org.hyperic.sigar.CpuPerc;
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
    private static double cpu;
    private static boolean isWindows;
    private static int connectCountTotal;
    private static int connectCountActive;

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
            cpu = osMxBean.getSystemCpuLoad();

        } else {
            Sigar sigar = new Sigar();
            double cpuTotal = 0;
            CpuPerc[] cpuList = null;
            cpuList = sigar.getCpuPercList();
            for (int i = 0; i < cpuList.length; i++) {
                String cpuString = CpuPerc.format(cpuList[i].getCombined());
                if (cpuString.contains("N")) {
                    cpuString = "100%";
                }
                cpuTotal += Double.valueOf(cpuString.substring(0, cpuString.length() - 1)) / 100;
            }
            setCpu(cpuTotal / cpuList.length);

            /*FileSystem[] fsList = sigar.getFileSystemList();
            for (int i = 0; i < fsList.length; i++) {
                FileSystem fs = fsList[i];
                FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                if (fs.getType() == 2) {
                    // 文件系统总大小
                    System.out.println(fs.getDevName() + "总大小:    " + usage.getTotal() + "KB");
                    // 文件系统剩余大小
                    System.out.println(fs.getDevName() + "剩余大小:    " + usage.getFree() + "KB");
                    // 文件系统可用大小
                    System.out.println(fs.getDevName() + "可用大小:    " + usage.getAvail() + "KB");
                    // 文件系统已经使用量
                    System.out.println(fs.getDevName() + "已经使用量:    " + usage.getUsed() + "KB");
                    double usePercent = usage.getUsePercent() * 100D;
                    // 文件系统资源的利用率
                    System.out.println(fs.getDevName() + "资源的利用率:    " + usePercent + "%");
                }
            }
            
            String[] ifNames = sigar.getNetInterfaceList();
            for (int i = 0; i < ifNames.length; i++) {
                String name = ifNames[i];
                NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
                System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets());// 接收的总包裹数
                System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets());// 发送的总包裹数
                System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
                System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes());// 发送的总字节数
                System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors());// 接收到的错误包数
                System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors());// 发送数据包时的错误数
                System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped());// 接收时丢弃的包数
                System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped());// 发送时丢弃的包数
            }*/

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
