package client;  

import java.io.UnsupportedEncodingException;
import java.util.Arrays; 


/**
 * StringConstructor class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class StringConstructor {
	public static byte[] constructor(int status, int len) throws UnsupportedEncodingException {
		int bytall = 8000;
		
		byte[] b = new byte[bytall];
		//Arrays.fill(b, (byte) 0);
		//Arrays.fill(b, (byte) (48));
		for (int i = 0; i < len; i++) {
			b[i + 8] = (byte) 48;
		}
		for (int i = len + 8; i < bytall; i++) {
			b[i] = (byte) 0;
		}
		b[3] = (byte) (status & 0xff);
		
		b[4] = (byte) ((len >> 24) & 0xff);
		b[5] = (byte) ((len >> 16) & 0xff);
		b[6] = (byte) ((len >> 8) & 0xff);
		b[7] = (byte) (len & 0xff);
		
		return b;
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(constructor(1,432));
	}
}