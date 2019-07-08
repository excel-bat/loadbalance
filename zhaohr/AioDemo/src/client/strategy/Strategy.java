package strategy;

import java.util.List;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/08
 */
public interface Strategy {

    ServerInfo getNextServer(List<ServerInfo> serverList);

}
