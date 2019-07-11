package strategy;

import java.util.List;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/11
 */
public class MinConnectActiveStrategy implements Strategy {

    @Override
    public ServerInfo getNextServer() {
        List<ServerInfo> serverList = ServerInfo.serverList;
        ServerInfo result = null;
        int minConnect = 100;
        for (ServerInfo serverInfo : serverList) {
            int connect = serverInfo.getConnectCountActive();
            System.out.print(String.valueOf(connect) + " ");
            if (connect < minConnect) {
                minConnect = connect;
                result = serverInfo;
            }
        }
        System.out.print(serverList.indexOf(result));
        System.out.println();
        return result;
    }

}
