package speed;


import java.util.Random;


/**
 * SpeedConst class
 * 
 * @author LiWeiqi
 * @date 2019/07/26
 */
public class SpeedConst implements Speed {
	//private static SpeedConst speedConst;
	public SpeedConst() {
	}
	//public static SpeedConst getInstance() {
	//	if (null == speedConst) {
	//		speedConst = new SpeedConst();
	//	}
	//	return speedConst;
	//}
	@Override
	public int getSleep() {
		int n = 10;
        return n; 
	}
}
