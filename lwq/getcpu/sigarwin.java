
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Who;
import java.io.File;

import org.hyperic.sigar.Sigar;
//import com.eos.system.annotation.Bizlet;

class SigarUtils {
    //public final static Sigar sigar = initSigar();
    public static Sigar initSigar() {
        try {
            //String file = Paths.get(PathKit.getWebRootPath(),  "files", "sigar",".sigar_shellrc").toString();
            //File classPath = new File(file).getParentFile();
            //String filePath = SigarUtils.class.getClassLoader().getResource("").toURI().getPath();
            //System.out.println(filePath);
            File classPath = new File("C:/primeton/platform6/ide/eclipse/workspace/ywlcyz/src/org/gocom/components/test/sigar");

            String path = System.getProperty("java.library.path");
            String sigarLibPath = classPath.getCanonicalPath();
            if (!path.contains(sigarLibPath)) {
                if (isOSWin()) {
                    path += ";" + sigarLibPath;System.out.println(path);
                } else {
                    path += ":" + sigarLibPath;
                }
                System.setProperty("java.library.path", path);
            }
            return new Sigar();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isOSWin(){
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return true;
        } else return false;
    }
}
public class sigarwin {
    public static void main(String[] args) {
        try {
            System.out.println("----------------------------------");
            // cpu
            while (true) {
                Thread.sleep(1000);  
                cpu();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
    }
    public static double getCPU() throws SigarException {
    	Sigar sigar = new Sigar();
        CpuInfo infos[] = sigar.getCpuInfoList();
        CpuPerc cpuList[] = null;
        cpuList = sigar.getCpuPercList();
        double sum = 0.0;
        for (int i = 0; i < infos.length; i++) {
            CpuInfo info = infos[i];
            sum = sum + cpuList[i].getCombined();
        }
        return (sum / infos.length);
    }

    private static void cpu() throws SigarException {
        Sigar sigar = new Sigar();
        CpuInfo infos[] = sigar.getCpuInfoList();
        CpuPerc cpuList[] = null;
        cpuList = sigar.getCpuPercList();
        double sum = 0.0;
        for (int i = 0; i < infos.length; i++) {
            CpuInfo info = infos[i];
            sum = sum + cpuList[i].getCombined();
        }
        System.out.println(sum / infos.length);
    }

}