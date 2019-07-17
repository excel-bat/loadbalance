package schedule.strategy;

/**
 * StrategyUtils class
 * 
 * @author LiWeiqi
 * @date 2019/07/08
 */
public class StrategyUtils {
	public static Strategy getStrategy(String type) {
		Strategy instance = null;
		if ("random".equals(type)) {
			System.out.println("Using strategy: random");
			instance = (Strategy) StrategyRandom.getInstance();			
		}
		if ("rr".equals(type)) {
			System.out.println("Using strategy: rr");
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
		if ("rrwithcore".equals(type)) {
			System.out.println("Using strategy: rrwithcore");
			instance = (Strategy) StrategyRrWithCore.getInstance();			
		}
		return instance;
	}
}
