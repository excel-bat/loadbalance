package speed;


import java.util.Random;


/**
 * SpeedRandom class
 * 
 * @author LiWeiqi
 * @date 2019/07/26
 */
public class SpeedRandom implements Speed {
	//private static SpeedRandom speedRandom;
	public SpeedRandom() {
	}
	//public static SpeedRandom getInstance() {
	//	if (null == speedRandom) {
	//		speedRandom = new SpeedRandom();
	//	}
	//	return speedRandom;
	//}
	@Override
	public int getSleep() {
		int n = 1000;
		Random rand=new Random();
        return rand.nextInt(5 + n); 
	}
}
