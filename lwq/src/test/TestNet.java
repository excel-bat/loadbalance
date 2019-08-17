package test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;  
    import java.util.List;  
      
    import org.hyperic.sigar.NetInterfaceConfig;  
    import org.hyperic.sigar.NetInterfaceStat;  
    import org.hyperic.sigar.Sigar;  
    import org.hyperic.sigar.SigarException;  
      
      
    /** 
     * 网卡信息、接口数据、流量 
     *  
     * 使用Sigar获得网卡信息 
     *  
     */  
    public class TestNet {  
      
        private NetInterfaceConfig config;  
        private NetInterfaceStat stat;  
        private long rxbps;  
        private long txbps;  
      
        public TestNet() {}  
      
        public void populate(Sigar sigar, String name)  
            throws SigarException {  
      
            config = sigar.getNetInterfaceConfig(name);  
      
            try {  
                  
                long start = System.currentTimeMillis();  
                NetInterfaceStat statStart = sigar.getNetInterfaceStat(name);  
                long rxBytesStart = statStart.getRxBytes();  
                long txBytesStart = statStart.getTxBytes();  
                Thread.sleep(1000);  
                long end = System.currentTimeMillis();  
                NetInterfaceStat statEnd = sigar.getNetInterfaceStat(name);  
                long rxBytesEnd = statEnd.getRxBytes();  
                long txBytesEnd = statEnd.getTxBytes();  
                  
                rxbps = (rxBytesEnd - rxBytesStart)*8/(end-start)*1000;  
                txbps = (txBytesEnd - txBytesStart)*8/(end-start)*1000;  
                stat = sigar.getNetInterfaceStat(name);  
            } catch (SigarException e) {  
                  
            } catch (Exception e) {  
                  
            }  
        }  
      
        public static TestNet gather(Sigar sigar, String name)  
            throws SigarException {  
          
        	TestNet data = new TestNet();  
            data.populate(sigar, name);  
            return data;  
        }  
      
        public NetInterfaceConfig getConfig() {  
            return config;  
        }  
      
        public NetInterfaceStat getStat() {  
            return stat;  
        }  
          
          
          
        public long getRxbps() {  
            return rxbps;  
        }  
      
        public long getTxbps() {  
            return txbps;  
        }  
      
        public static void main(String[] args) throws Exception {  
            Sigar sigar = new Sigar();  
            String[] netIfs = sigar.getNetInterfaceList();  
            List netIfList = new ArrayList();  
            for ( String name:netIfs ) {  
            	
            	TestNet netIfData1 = TestNet.gather(sigar, name);  
                netIfList.add(netIfData1);  
                
                NetworkInterface ni = NetworkInterface.getByName(name);
                System.out.println(name + " " + ni + " rxbps: " + netIfData1.rxbps + " txbps: " + netIfData1.txbps);
            } 
            
            
            //XStream xstream = new XStream();  
            //xstream.alias("NetInterfaceDatas", List.class);  
            //xstream.alias("NetInterfaceData", NetInterfaceData.class);  
            //System.out.println(xstream.toXML(netIfList));  
        }  
    }