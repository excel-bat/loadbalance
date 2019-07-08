package schedule.strategy;

public class StrategyUtils {
	public static Strategy getStrategy(String type) {
		Strategy instance = null;
		if ("random".equals(type)) {
			System.out.println("Using strategy: random");
			instance = (Strategy) StrategyRandom.getInstance();			
		}
		return instance;
	}
}
