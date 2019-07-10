package strategy;

import java.util.List;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class LessCpuStrategy implements Strategy {

    @Override
    public ServerInfo getNextServer() {
        List<ServerInfo> serverList = ServerInfo.serverList;
        ServerInfo result = null;
        double minCpu = 1;
        for (ServerInfo serverInfo : serverList) {
            double cpu = serverInfo.getCpu();
            if (cpu <= minCpu) {
                minCpu = cpu;
                result = serverInfo;
            }
        }
        return result;
    }

}
