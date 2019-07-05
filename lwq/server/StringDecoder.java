package server;  

/**
 * StringDecoder class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class StringDecoder {
	public static int[] decoder(String str) {
		int status = (int)str.charAt(0) - (int)('0');
		String sub = str.substring(1, str.indexOf(0));
		int len = Integer.parseInt(sub);
		return new int[] {status, len};
	}
}