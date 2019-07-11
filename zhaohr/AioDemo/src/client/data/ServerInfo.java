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
    private int connectCountActive;
    public int weight;
    public int effectiveWeight;
    public int currentWeight;
    public static List<ServerInfo> serverList = new ArrayList<ServerInfo>();

    public ServerInfo(String ip, int filePort, int infoPort) {
        this.ip = ip;
        this.filePort = filePort;
        this.infoPort = infoPort;
        weight = 1;
        effectiveWeight = weight;
        currentWeight = 0;
    }

    public ServerInfo(String ip, int filePort, int infoPort, int weight) {
        this.ip = ip;
        this.filePort = filePort;
        this.infoPort = infoPort;
        this.weight = weight;
        effectiveWeight = weight;
        currentWeight = 0;
    }

    public static void setServerList() {
        serverList.add(new ServerInfo("10.2.0.211", 8000, 8001));
        // serverList.add(new ServerInfo("127.0.0.1", 8000, 8001));
        serverList.add(new ServerInfo("10.2.0.102", 8000, 8001));
        serverList.add(new ServerInfo("10.2.0.103", 8000, 8001));
        // serverList.add(new ServerInfo("10.2.0.101", 8000, 8001));

    }

    public synchronized double getCpu() {
        return cpu;
    }

    public synchronized void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public synchronized int getConnectCountActive() {
        return connectCountActive;
    }

    public synchronized void setConnectCountActive(int count) {
        this.connectCountActive = count;
    }

}
