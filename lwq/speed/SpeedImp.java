package speed;


import java.util.Random;


/**
 * SpeedRandom class
 * 
 * @author LiWeiqi
 * @date 2019/07/26
 */
public class SpeedImp implements Speed {
	//private static SpeedRandom speedRandom;
	int counter;
	int s1;
	int s2;
	public SpeedImp() {
		counter = 0;
		s1 = 1;
		s2 = 20;
	}
	//public static SpeedRandom getInstance() {
	//	if (null == speedRandom) {
	//		speedRandom = new SpeedRandom();
	//	}
	//	return speedRandom;
	//}
	@Override
	public int getSleep() {
		counter++;
		if (counter > 100) {
			counter = 0;
		}
		if (counter < 70) {
			return s1;
		} else {
			return s2;
		}
	}
}
