package server;

import java.io.UnsupportedEncodingException;

/**
 * StringDecoder class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class StringDecoder {
	public static int[] decoder(byte[] b) throws UnsupportedEncodingException {
		int status = 0;
		int len = 0;
		
		status = (b[3] & 0xff);
		
		len = (b[4] & 0xff) << 24 | 
			  (b[5] & 0xff) << 16 | 
			  (b[6] & 0xff) << 8  | 
			  (b[7] & 0xff);
		return new int[] {status, len};
	}
}