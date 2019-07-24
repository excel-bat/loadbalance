package strategy;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/24
 */
public class CovAutoWeightStrategy implements Strategy {
    private static final int M = 12;
    private static final int WEIGHTL = 1, WEIGHTH = 100;
    private WeightRoundRobinStrategy weightRoundRobinStrategy = new WeightRoundRobinStrategy();
    private int serverLength = ServerInfo.serverList.size();
    private double[][] connect = new double[serverLength][M], cpu = new double[serverLength][M],
        disk = new double[serverLength][M], memory = new double[serverLength][M], process = new double[serverLength][M],
        response = new double[serverLength][M], speed = new double[serverLength][M],
        wrongAtServer = new double[serverLength][M], wrongAtClient = new double[serverLength][M];
    private double[] load = new double[serverLength];
    private int[] weight = new int[serverLength];
    private int count = 0;
    double connectW = 1, cpuW = 1, diskW = 1, memoryW = 1, processW = 1, speedW = 1, wrongAtServerW = 1,
        wrongAtClientW = 1;

    private double getCov(double[] x, double[] y) {
        int length = x.length;
        double[] xy = new double[length];
        double Ex = 0, Ey = 0, Exy = 0;
        for (int i = 0; i < length; i++) {
            xy[i] = x[i] * y[i];
            Ex += x[i];
            Ey += y[i];
            Exy += xy[i];
        }
        Ex /= length;
        Ey /= length;
        Exy /= length;
        double result = Exy - Ex * Ey;
        return result;
    }

    private double getW(double[] x, double[] y) {
        double zero = Math.sqrt(getCov(x, x)) * Math.sqrt(getCov(y, y));
        if (zero == 0) {
            return 0;
        } else {
            return Math.abs(getCov(x, y) / zero);
        }
    }

    @Override
    public ServerInfo getNextServer() {
        ServerInfo result = weightRoundRobinStrategy.getNextServer();
        return result;
    }

    @Override
    public void setWeight() {
        int j = count % M;
        for (int i = 0; i < serverLength; i++) {
            ServerInfo serverInfo = ServerInfo.serverList.get(i);
            connect[i][j] = serverInfo.getConnectActiveServer();
            cpu[i][j] = serverInfo.getCpu();
            disk[i][j] = serverInfo.getDev();
            memory[i][j] = serverInfo.getMemory();
            process[i][j] = serverInfo.getProcess();
            response[i][j] = serverInfo.getUnitTime();
            speed[i][j] = serverInfo.getRxSpeed();
            wrongAtServer[i][j] = (double)serverInfo.getConnectFinishedServer()
                / (serverInfo.getConnectFinishedServer() + serverInfo.getConnectFailedServer());
            wrongAtClient[i][j] = (double)serverInfo.getConnectFinishedClient()
                / (serverInfo.getConnectFinishedClient() + serverInfo.getConnectFailedClient());
        }
        if (count < M - 1) {
            return;
        } else {
            double loadTotal = 0;
            int jlast = (count - 1) % M;
            for (int i = 0; i < serverLength; i++) {
                if (connect[jlast] != connect[j]) {
                    connectW = getCov(connect[i], response[i]);
                }
                if (cpu[jlast] != cpu[j]) {
                    cpuW = getCov(cpu[i], response[i]);
                }
                if (disk[jlast] != disk[j]) {
                    diskW = getCov(disk[i], response[i]);
                }
                if (memory[jlast] != memory[j]) {
                    memoryW = getCov(disk[i], response[i]);
                }
                if (process[jlast] != process[j]) {
                    processW = getCov(process[i], response[i]);
                }
                if (speed[jlast] != speed[j]) {
                    speedW = getCov(speed[i], response[i]);
                }
                if (wrongAtServer[jlast] != wrongAtServer[j]) {
                    wrongAtServerW = getCov(wrongAtServer[i], response[i]);
                }
                if (wrongAtClient[jlast] != wrongAtClient[j]) {
                    wrongAtClientW = getCov(wrongAtClient[i], response[i]);
                }
                double totalW = connectW + cpuW + diskW + memoryW + processW + speedW + wrongAtServerW + wrongAtClientW;
                load[i] = connectW / totalW * connect[i][j] + cpuW / totalW * cpu[i][j] + diskW / totalW * disk[i][j]
                    + memoryW / totalW * memory[i][j] + processW / totalW * process[i][j]
                    + speedW / totalW * speed[i][j] + wrongAtServerW / totalW * wrongAtServer[i][j]
                    + wrongAtClientW / totalW * wrongAtClient[i][j];
                loadTotal += load[i];
            }
            for (int i = 0; i < serverLength; i++) {
                weight[i] = (int)(loadTotal / M / load[i] * 100);
                weight[i] = Math.max(weight[i], WEIGHTL);
                weight[i] = Math.min(weight[i], WEIGHTH);
            }
            ServerInfo.setWeight(weight);
        }
        count++;
    }

}
