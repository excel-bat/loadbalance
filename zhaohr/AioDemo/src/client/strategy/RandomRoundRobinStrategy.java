package strategy;

import java.util.List;
import java.util.Random;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/11
 */
public class RandomRoundRobinStrategy implements Strategy {

    private int i = new Random().nextInt(ServerInfo.serverList.size());

    @Override
    public ServerInfo getNextServer() {
        List<ServerInfo> serverList = ServerInfo.serverList;
        if (i == serverList.size() - 1) {
            i = 0;
        } else {
            i += 1;
        }
        return serverList.get(i);
    }

}
