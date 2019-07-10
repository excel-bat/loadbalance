package strategy;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/08
 */
public class StrategySelector {
    Strategy strategy;

    public StrategySelector() {
        strategy = new RandomStrategy();
    }

    public ServerInfo getNextServer() {
        return strategy.getNextServer();
    }

}