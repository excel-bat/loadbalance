package schedule.strategy;

public class StrategyUtils {
	public static Strategy getStrategy(String type) {
		Strategy instance = null;
		if ("random".equals(type)) {
			System.out.println("Using strategy: random");
			instance = (Strategy) StrategyRandom.getInstance();			
		}
		if ("poll".equals(type)) {
			System.out.println("Using strategy: poll");
			instance = (Strategy) StrategyPoll.getInstance();			
		}
		if ("cpumin".equals(type)) {
			System.out.println("Using strategy: cpumin");
			instance = (Strategy) StrategyCpuMin.getInstance();			
		}
		if ("cpuavimin".equals(type)) {
			System.out.println("Using strategy: cpuavimin");
			instance = (Strategy) StrategyCpuAviMin.getInstance();			
		}
		return instance;
	}
}
