package strategy;

import java.util.List;
import java.util.Random;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/08
 */
public class RandomStrategy implements Strategy {

    @Override
    public ServerInfo getNextServer(List<ServerInfo> serverList) {
        Random random = new Random();
        int i = random.nextInt(serverList.size());
        return serverList.get(i);
    }

}
