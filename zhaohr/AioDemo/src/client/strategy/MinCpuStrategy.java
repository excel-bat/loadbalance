package strategy;

import java.util.List;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class MinCpuStrategy implements Strategy {

    @Override
    public ServerInfo getNextServer() {
        List<ServerInfo> serverList = ServerInfo.serverList;
        ServerInfo result = null;
        double minCpu = 1;
        for (ServerInfo serverInfo : serverList) {
            double cpu = serverInfo.getCpu();
            // System.out.print(String.valueOf(cpu) + " ");
            if (cpu <= minCpu) {
                minCpu = cpu;
                result = serverInfo;
            }
        }
        // System.out.print(serverList.indexOf(result));
        // System.out.println();
        return result;
    }

    @Override
    public void setWeight() {
        // TODO Auto-generated method stub

    }

}
