package strategy;

import java.util.Random;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/17
 */
public class TimeStrategy implements Strategy {

    @Override
    public ServerInfo getNextServer() {
        ServerInfo result = null;
        int size = ServerInfo.serverList.size();
        double[] rateList = new double[size];
        long total = 0;
        for (int i = 0; i < size; i++) {
            total += ServerInfo.serverList.get(i).getUnitTime();
        }
        rateList[0] = (double)total / ServerInfo.serverList.get(0).getUnitTime();
        for (int i = 1; i < size; i++) {
            rateList[i] = (double)total / ServerInfo.serverList.get(i).getUnitTime();
            rateList[i] = rateList[i - 1] + rateList[i];
        }
        // System.out.print(ServerInfo.serverList.get(0).unitTime + "--" + rateList[0] + " ");
        for (int i = 1; i < size; i++) {
            // System.out.print(ServerInfo.serverList.get(i).unitTime + "--" + (rateList[i] - rateList[i - 1]) + " ");
        }
        // System.out.println();
        double rate = new Random().nextDouble() * rateList[size - 1];
        for (int i = 0; i < size; i++) {
            if (rate < rateList[i]) {
                // System.out.println();
                // System.out.println(rate + " " + i);
                result = ServerInfo.serverList.get(i);
                break;
            }
        }

        return result;

    }

}
