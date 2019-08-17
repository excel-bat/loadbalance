package speed;



/**
 * SpeedUtils class
 * 
 * @author LiWeiqi
 * @date 2019/08/12
 */
public class SpeedUtils {
	public static Speed getSpeed(String type) {
		Speed instance = null;
		if (true) {
			System.out.println("Using speed strategy: Const");
			//instance = (Speed) new SpeedRandom();			
			//instance = (Speed) new SpeedConst();
			instance = (Speed) new SpeedImp();
		}
		return instance;
	}
}
