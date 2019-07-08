package strategy;

import java.util.List;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/08
 */
public class PollStrategy implements Strategy {

    private int i = -1;

    @Override
    public ServerInfo getNextServer(List<ServerInfo> serverList) {
        if (i == serverList.size()) {
            i = 0;
        } else {
            i += 1;
        }
        return serverList.get(i);
    }

}