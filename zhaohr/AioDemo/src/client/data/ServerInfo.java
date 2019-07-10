package data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaohr16
 * @date 2019/07/08
 */
public class ServerInfo {

    public String ip;
    public int filePort;
    public int infoPort;
    private double cpu;
    public static List<ServerInfo> serverList = new ArrayList<ServerInfo>();

    public ServerInfo(String ip, int filePort, int infoPort) {
        this.ip = ip;
        this.filePort = filePort;
        this.infoPort = infoPort;
    }

    public static void setServerList() {
        serverList.add(new ServerInfo("192.168.56.1", 8000, 8001));
        // serverList.add(new ServerInfo("127.0.0.1", 8000, 8001));
        serverList.add(new ServerInfo("192.168.52.137", 8000, 8001));
        serverList.add(new ServerInfo("192.168.52.138", 8000, 8001));

    }

    public synchronized double getCpu() {
        return cpu;
    }

    public synchronized void setCpu(double cpu) {
        this.cpu = cpu;
    }

}
