package strategy;

import java.util.List;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/11
 */
public class WeightRoundRobinStrategy implements Strategy {

    @Override
    public ServerInfo getNextServer() {
        List<ServerInfo> serverList = ServerInfo.serverList;
        int totalWeight = 0;
        int maxCurrentWeight = 0;
        ServerInfo result = null;
        for (ServerInfo serverInfo : serverList) {
            totalWeight += serverInfo.effectiveWeight;
            serverInfo.currentWeight = serverInfo.currentWeight + serverInfo.effectiveWeight;
            if (maxCurrentWeight <= serverInfo.currentWeight) {
                maxCurrentWeight = serverInfo.currentWeight;
                result = serverInfo;
            }
        }
        result.currentWeight -= totalWeight;
        if (result.effectiveWeight < result.weight) {
            result.effectiveWeight++;
        }
        return result;
    }

}
