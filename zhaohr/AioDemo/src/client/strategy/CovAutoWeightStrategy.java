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
    double connectW = 0, cpuW = 0, diskW = 0, memoryW = 0, processW = 0, speedW = 0, wrongAtServerW = 0,
        wrongAtClientW = 0;

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
        double zero = Math.sqrt(Math.abs(getCov(x, x))) * Math.sqrt(Math.abs(getCov(y, y)));
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
            count++;
            return;
        } else {
            double loadTotal = 0;
            int jlast = (count - 1) % M;
            for (int i = 0; i < serverLength; i++) {
                if (connect[i][jlast] != connect[i][j]) {
                    connectW = getW(connect[i], response[i]);
                }
                if (cpu[i][jlast] != cpu[i][j]) {
                    cpuW = getW(cpu[i], response[i]);
                }
                if (disk[i][jlast] != disk[i][j]) {
                    diskW = getW(disk[i], response[i]);
                }
                if (memory[i][jlast] != memory[i][j]) {
                    memoryW = getW(disk[i], response[i]);
                }
                if (process[i][jlast] != process[i][j]) {
                    processW = getW(process[i], response[i]);
                }
                if (speed[i][jlast] != speed[i][j]) {
                    speedW = getW(speed[i], response[i]);
                }
                if (wrongAtServer[i][jlast] != wrongAtServer[i][j]) {
                    wrongAtServerW = getW(wrongAtServer[i], response[i]);
                }
                if (wrongAtClient[i][jlast] != wrongAtClient[i][j]) {
                    wrongAtClientW = getW(wrongAtClient[i], response[i]);
                }
                // double totalW = connectW + cpuW + diskW + memoryW + processW + speedW + wrongAtServerW +
                // wrongAtClientW;
                double totalW = cpuW + diskW + memoryW + wrongAtClientW;
                /*load[i] = connectW / totalW * connect[i][j] + cpuW / totalW * cpu[i][j] + diskW / totalW * disk[i][j]
                    + memoryW / totalW * memory[i][j] + processW / totalW * process[i][j]
                    + speedW / totalW * speed[i][j]
                    + wrongAtServerW / totalW * wrongAtServer[i][j] + wrongAtClientW / totalW * wrongAtClient[i][j];*/
                load[i] = cpuW / totalW * cpu[i][j] + diskW / totalW * disk[i][j] + memoryW / totalW * memory[i][j]
                    + wrongAtClientW / totalW * wrongAtClient[i][j];
                loadTotal += load[i];
                /*System.out.println(connectW / totalW * connect[i][j] + " " + cpuW / totalW * cpu[i][j] + " " + diskW / totalW * disk[i][j]
                        + " " + memoryW / totalW * memory[i][j] + " " + processW / totalW * process[i][j]
                                + " " + speedW / totalW * speed[i][j] + " " + wrongAtServerW / totalW * wrongAtServer[i][j]
                                + " " + wrongAtClientW / totalW * wrongAtClient[i][j]);*/

            }
            for (int i = 0; i < serverLength; i++) {
                if (!Float.isNaN((float)load[i])) {
                    weight[i] = (int)(loadTotal / M / load[i] * 100);
                    weight[i] = Math.max(weight[i], WEIGHTL);
                    weight[i] = Math.min(weight[i], WEIGHTH);
                }
                // System.out.println(weight[i] + " "+load[i]);
            }
            // System.out.println();
            ServerInfo.setWeight(weight);
            count++;
        }
    }

}
