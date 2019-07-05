package client;  

import java.util.Arrays; 


/**
 * StringConstructor class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class StringConstructor {
	public static String constructor(int status, int len) {
		String str="" + status + len;
		int bytall = 8192;
		int byt1 = str.getBytes().length;
		int byt2 = bytall - byt1;
		byte[] emptyb = new byte[byt2];
		Arrays.fill(emptyb, (byte)0);
		String emptystr = new String(emptyb);
		str = str + emptystr;
		return str;
	}
}