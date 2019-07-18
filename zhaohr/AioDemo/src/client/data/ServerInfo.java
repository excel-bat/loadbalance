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
    public long startTime = 0;
    public long endTime = 0;
    public long unitTime = 1;
    public volatile int connectSend = 0;
    public volatile int connectFinished = 0;
    public volatile int connectFailed = 0;
    public volatile double connectServerWrongRate = 0;
    public volatile double connectClientWrongRate = 0;
    public static volatile int connectSendTotal = 0;
    public static volatile int connectFinishedTotal = 0;
    public static volatile int connectFailedTotal = 0;
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
        serverList.add(new ServerInfo("10.2.0.211", 8000, 8001, 1));
        // serverList.add(new ServerInfo("127.0.0.1", 8000, 8001));
        // serverList.add(new ServerInfo("10.2.0.102", 8000, 8001));
        // serverList.add(new ServerInfo("10.2.0.103", 8000, 8001));
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
