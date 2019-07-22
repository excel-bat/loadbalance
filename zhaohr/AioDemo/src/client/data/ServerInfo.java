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
    private double cpu = 0;
    private int connectCountActive;
    private double memory = 0;
    private long rxBytes = 0;// 流量总数，需要取段处理
    private double rxSpeed = 0;
    private double dev = 0;
    public int weight;
    public int effectiveWeight;
    public int currentWeight;
    public long startTime = 0;
    public long endTime = 0;
    private long unitTime = 1;
    private int connectSend = 0;
    private int connectFinished = 0;
    private int connectFailed = 0;
    public double connectServerWrongRate = 0;
    public double connectClientWrongRate = 0;
    private static int connectSendTotal = 0;
    private static int connectFinishedTotal = 0;
    private static int connectFailedTotal = 0;
    public static final int INTERVAL = 50;
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

    public void resetWeight(int weight) {
        this.weight = weight;
        effectiveWeight = weight;
        currentWeight = 0;
    }

    public static void setServerList() {
        // serverList.add(new ServerInfo("10.2.0.211", 8000, 8001, 1));
        // serverList.add(new ServerInfo("127.0.0.1", 8000, 8001));
        serverList.add(new ServerInfo("10.2.0.102", 8000, 8001));
        serverList.add(new ServerInfo("10.2.0.103", 8000, 8001));
        serverList.add(new ServerInfo("10.2.0.101", 8000, 8001));

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

    public synchronized double getMemory() {
        return memory;
    }

    public synchronized void setMemory(double memory) {
        this.memory = memory;
    }

    public synchronized long getRxBytes() {
        return rxBytes;
    }

    public synchronized void setRxBytes(long rxBytes) {
        rxSpeed = (double)(rxBytes - this.rxBytes) / INTERVAL;
        this.rxBytes = rxBytes;
    }

    public synchronized double getRxSpeed() {
        return rxSpeed;
    }

    public synchronized double getDev() {
        return dev;
    }

    public synchronized void setDev(double dev) {
        this.dev = dev;
    }

    public synchronized void setUnitTime(long time) {
        this.unitTime = time;
    }

    public synchronized long getUnitTime() {
        return unitTime;
    }

    public synchronized void addConnectSend() {
        connectSend++;
        connectSendTotal++;
    }

    public synchronized int getConnectSend() {
        return connectSend;
    }

    public synchronized void addConnectFinished() {
        connectFinished++;
        connectFinishedTotal++;
    }

    public synchronized int getConnectFinished() {
        return connectFinished;
    }

    public synchronized void addConnectFailed() {
        connectFailed++;
        connectFailedTotal++;
    }

    public synchronized int getConnectFailed() {
        return connectFailed;
    }

    public static synchronized int getConnectSendTotal() {
        return connectSendTotal;
    }

    public static synchronized int getConnectFinishedTotal() {
        return connectFinishedTotal;
    }

    public static synchronized int getConnectFailedTotal() {
        return connectFailedTotal;
    }
}
