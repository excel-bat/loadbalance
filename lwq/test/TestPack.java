package test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import client.StringConstructor;  
import server.StringDecoder;  
/** 
 * ≤‚ ‘∑Ω∑® 
 * @author yangtao__anxpp.com 
 * @version 1.0 
 */  
public class TestPack {  
    /**
     * ≤‚ ‘strpack
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{  
        //byte[] ret = StringConstructor.constructor(0, 439992);
        //int[] ans = StringDecoder.decoder(ret);
        //System.out.println(ans[0]);
        //System.out.println(ans[1]);
        
        String tt = "a";
        //System.out.println(tt);
        byte[] t = tt.getBytes(); 
        //System.out.println(t.length);
        String body = new String(t,"UTF-8");
        //System.out.println(body);
        
        byte[] content = new byte[10];
		Arrays.fill(content, (byte) (48));
		body = new String(content,"UTF-8");
        //System.out.println(body);
        
        Date currentTime = new Date(); 
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss"); 
        String s8 = formatter.format(currentTime);
        //System.out.println(s8);
        int len = 10;
        byte[] b = new byte[len];
		Arrays.fill(b, (byte) (48));
		String info = new String(b,"UTF-8");
		String ttt = "" + len + "," + s8 + "," + info + "\n"; 
		System.out.println(ttt);
		String[] split=ttt.split(","); 
		System.out.println(split[0]);
		System.out.println(split[1]);
		System.out.println(split[2]);
    }  
}  