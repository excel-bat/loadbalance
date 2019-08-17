package client;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * ReadResult class
 * 
 * @author LiWeiqi
 * @date 2019/07/05
 */
public class ReadResult {
	public static String show(int len, byte[] data) {
		if (data[3] == 4) {
			return "server received";
		}
		if (len == 0) {
			return "None data";
		}
		byte[] b = new byte[len];
		Arrays.fill(b, (byte) (48));
		try {
			return new String(b,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return "data error";
	}
}