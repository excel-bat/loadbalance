package strategy;

import data.ServerInfo;

/**
 * @author zhaohr16
 * @date 2019/07/08
 */
public class StrategySelector {
    public static Strategy strategy;

    public StrategySelector() {
        strategy = new RandomStrategy();
    }

    public static ServerInfo getNextServer() {
        return strategy.getNextServer();
    }

    public static void setWeight() {
        strategy.setWeight();
    }

}