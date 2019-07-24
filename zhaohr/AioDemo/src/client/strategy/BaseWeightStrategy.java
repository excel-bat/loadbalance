package strategy;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/24
 */
public class BaseWeightStrategy implements Strategy {
    private static final double R1 = 0.25, R2 = 0.25, R3 = 0.05, R4 = 0.05, R5 = 0.15, R6 = 0.25;
    private static final double BETA = 0.5, BETAL = 0.4, BETAH = 0.6, DELTA = 0.191, WEIGHTL = 1, WEIGHTH = 100;
    private WeightRoundRobinStrategy weightRoundRobinStrategy = new WeightRoundRobinStrategy();
    private int serverLength = ServerInfo.serverList.size();
    private double[] connect = new double[serverLength], cpu = new double[serverLength],
        disk = new double[serverLength], memory = new double[serverLength], process = new double[serverLength],
        response = new double[serverLength], load = new double[serverLength];
    private int[] weight = new int[serverLength];

    @Override
    public ServerInfo getNextServer() {
        ServerInfo result = weightRoundRobinStrategy.getNextServer();
        return result;
    }

    @Override
    public void setWeight() {

        double connectTotal = 0, processTotal = 0, responseTotal = 0;
        for (int i = 0; i < serverLength; i++) {
            ServerInfo serverInfo = ServerInfo.serverList.get(i);
            weight[i] = serverInfo.weight;
            connect[i] = serverInfo.getConnectActiveServer();
            cpu[i] = serverInfo.getCpu();
            disk[i] = serverInfo.getDev();
            memory[i] = serverInfo.getMemory();
            process[i] = weight[i] * serverInfo.getProcess();
            response[i] = weight[i] * serverInfo.getUnitTime();

            connectTotal += connect[i];
            processTotal += process[i];
            responseTotal += response[i];
        }

        for (int i = 0; i < serverLength; i++) {
            connect[i] /= connectTotal;
            process[i] /= processTotal;
            response[i] /= responseTotal;
            load[i] =
                R1 * connect[i] + R2 * cpu[i] + R3 * disk[i] + R4 * memory[i] + R5 * process[i] + R6 * response[i];
            if (load[i] > BETAH) {
                weight[i] = (int)Math.max(WEIGHTL, (1 - DELTA) * weight[i]);
            } else if (load[i] < BETAL) {
                weight[i] = (int)Math.min(WEIGHTH, (1 + DELTA) * weight[i]);
            }
        }
        ServerInfo.setWeight(weight);

    }
}
