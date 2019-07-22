package strategy;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/19
 */
public class InfoStrategy implements Strategy {
    WeightRoundRobinStrategy weightRoundRobinStrategy = new WeightRoundRobinStrategy();

    private static double getCov(double[] x, double[] y) {
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

    private static double getW(double[] x, double[] y) {
        double zero = Math.sqrt(getCov(x, x)) * Math.sqrt(getCov(y, y));
        if (zero == 0) {
            return 0;
        } else {
            return Math.abs(getCov(x, y) / zero);
        }
    }

    public static void getWeight() {
        int length = ServerInfo.serverList.size();
        double[] cpu = new double[length];
        double[] memory = new double[length];
        double[] dev = new double[length];
        double[] speed = new double[length];
        double[] wrongAtServer = new double[length];
        double[] wrongAtClient = new double[length];
        double[] time = new double[length];
        double[] load = new double[length];
        for (int i = 0; i < length; i++) {
            ServerInfo serverInfo = ServerInfo.serverList.get(i);
            cpu[i] = serverInfo.getCpu();
            memory[i] = serverInfo.getMemory();
            dev[i] = serverInfo.getDev();
            speed[i] = serverInfo.getRxSpeed();
            wrongAtServer[i] = serverInfo.connectServerWrongRate;
            wrongAtClient[i] = (double)serverInfo.getConnectFailed()
                / (serverInfo.getConnectFailed() + serverInfo.getConnectFinished() + 1);
            time[i] = (double)serverInfo.getUnitTime();
        }
        double cpuW = getW(cpu, time);
        double memoryW = getW(memory, time);
        double devW = getW(dev, time);
        double speedW = getW(speed, time);
        double wrongServerW = getW(wrongAtServer, time);
        double wrongClientW = getW(wrongAtClient, time);
        double minLoad = Double.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            load[i] = cpuW * cpu[i] + memoryW * memory[i] + devW * dev[i] + speedW * speed[i]
                + wrongServerW * wrongAtServer[i] + wrongClientW * wrongAtClient[i];
            if (load[i] < minLoad) {
                minLoad = load[i];
            }
        }

        for (int i = 0; i < length; i++) {
            int weight = (int)(minLoad / load[i]) * 100;
            ServerInfo.serverList.get(i).resetWeight(weight);
        }

    }

    @Override
    public ServerInfo getNextServer() {
        ServerInfo result = weightRoundRobinStrategy.getNextServer();
        return result;
    }

}
