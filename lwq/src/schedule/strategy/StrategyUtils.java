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
			instance = (Strategy) (new StrategyRandom());		
		}
		if ("rr".equals(type)) {
			System.out.println("Using strategy: rr");
			instance = (Strategy) (new StrategyPoll());			
		}
		if ("cpumin".equals(type)) {
			System.out.println("Using strategy: cpumin");
			instance = (Strategy) (new StrategyCpuMin());		
		}
		if ("cpuavimin".equals(type)) {
			System.out.println("Using strategy: cpuavimin");
			instance = (Strategy) (new StrategyCpuAviMin());		
		}
		if ("rrwithcore".equals(type)) {
			System.out.println("Using strategy: rrwithcore");
			instance = (Strategy) (new StrategyRrWithCore());		
		}
		if ("p2c".equals(type)) {
			System.out.println("Using strategy: p2c");	
			instance = (Strategy) (new StrategyP2C());		
		}
		if ("pickkx".equals(type)) {
			System.out.println("Using strategy: pickkx");
			instance = (Strategy) (new StrategyPickKX());		
		}
		if ("pickkxave".equals(type)) {
			System.out.println("Using strategy: pickkxave");
			instance = (Strategy) (new StrategyPickKXAve());		
		}
		if ("my".equals(type)) {
			System.out.println("Using strategy: my");
			instance = (Strategy) (new StrategyMy());		
		}
		return instance;
	}
}
