package strategy;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/08
 */
public class StrategySelector {
    public Strategy strategy;

    public StrategySelector() {
        strategy = new InfoStrategy();
    }

    public ServerInfo getNextServer() {
        return strategy.getNextServer();
    }

}