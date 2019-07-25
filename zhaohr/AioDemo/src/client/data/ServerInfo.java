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
    private double memory = 0;
    private long rxBytes = 0;// 流量总数，需要取段处理
    private double rxSpeed = 0;
    private double dev = 0;
    private int process = 0;
    public int weight;
    public int effectiveWeight;
    public int currentWeight;
    public long startTime = 0;
    public long endTime = 0;
    private long unitTime = 1;
    private int connectSendClient = 0;
    private int connectFinishedClient = 0;
    private int connectFailedClient = 0;
    private volatile int connectAccepctedServer = 0;
    private volatile int connectFinishedServer = 0;
    private volatile int connectFailedServer = 0;
    private volatile int connectActiveServer = 0;
    public double connectWrongRateClient = 0;
    public double connectWrongRateServer = 0;
    private static int connectSendTotal = 0;
    private static int connectFinishedTotal = 0;
    private static int connectFailedTotal = 0;
    public static final int INTERVAL = 200;
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

    public synchronized void resetWeight(int weight) {
        this.weight = weight;
        effectiveWeight = weight;
        currentWeight = 0;
    }

    public synchronized static void setWeight(int[] weight) {
        for (int i = 0; i < serverList.size(); i++) {
            ServerInfo serverInfo = serverList.get(i);
            serverInfo.weight = weight[i];
            serverInfo.effectiveWeight = weight[i];
            serverInfo.currentWeight = 0;
        }
    }

    public static void setServerList() {
        // serverList.add(new ServerInfo("10.2.0.211", 8000, 8001, 1));
        //serverList.add(new ServerInfo("127.0.0.1", 8000, 8001));
         serverList.add(new ServerInfo("10.2.0.102", 8000, 8001, 20));
         serverList.add(new ServerInfo("10.2.0.103", 8000, 8001, 5));
        // serverList.add(new ServerInfo("10.2.0.101", 8000, 8001));

    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }

    public long getRxBytes() {
        return rxBytes;
    }

    public void setRxBytes(long rxBytes) {
        rxSpeed = (double)(rxBytes - this.rxBytes) / INTERVAL / 1000;
        this.rxBytes = rxBytes;
    }

    public double getRxSpeed() {
        return rxSpeed;
    }

    public double getDev() {
        return dev;
    }

    public void setDev(double dev) {
        this.dev = dev;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public void setUnitTime(long time) {
        this.unitTime = time;
    }

    public long getUnitTime() {
        return unitTime;
    }

    public synchronized void addConnectSendClient() {
        connectSendClient++;
        connectSendTotal++;
    }

    public synchronized int getConnectSendClient() {
        return connectSendClient;
    }

    public synchronized void addConnectFinishedClient() {
        connectFinishedClient++;
        connectFinishedTotal++;
    }

    public synchronized int getConnectFinishedClient() {
        return connectFinishedClient;
    }

    public synchronized void addConnectFailedClient() {
        effectiveWeight--;
        connectFailedClient++;
        connectFailedTotal++;
    }

    public int getConnectAcceptedServer() {
        return connectAccepctedServer;
    }

    public void setConnectAcceptedServer(int connectAcceptedServer) {
        this.connectAccepctedServer = connectAcceptedServer;
    }

    public int getConnectFinishedServer() {
        return connectFinishedServer;
    }

    public void setConnectFinishedServer(int connectFinishedServer) {
        this.connectFinishedServer = connectFinishedServer;
    }

    public int getConnectFailedServer() {
        return connectFailedServer;
    }

    public void setConnectFailedServer(int connectAcceptedServer) {
        this.connectFailedServer = connectFailedServer;
    }

    public int getConnectActiveServer() {
        return connectActiveServer;
    }

    public void setConnectActiveServer() {
        this.connectActiveServer = connectAccepctedServer - connectFailedServer - connectFinishedServer;
    }

    public synchronized int getConnectFailedClient() {
        return connectFailedClient;
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
