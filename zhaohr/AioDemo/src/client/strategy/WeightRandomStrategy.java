package strategy;

import java.util.List;
import java.util.Random;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/11
 */
public class WeightRandomStrategy implements Strategy {

    @Override
    public ServerInfo getNextServer() {
        List<ServerInfo> serverList = ServerInfo.serverList;
        int totalWeight = 0;
        for (ServerInfo serverInfo : serverList) {
            totalWeight += serverInfo.weight;
        }

        Random random = new Random();
        int i = random.nextInt(totalWeight);
        int weight = 0;
        ServerInfo result = null;
        for (ServerInfo serverInfo : serverList) {
            if (i >= weight && i < weight + serverInfo.weight) {
                result = serverInfo;
                break;
            }
            weight += serverInfo.weight;
        }
        return result;
    }

}
