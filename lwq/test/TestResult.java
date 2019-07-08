package test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import server.Result;

/** 
 * ≤‚ ‘∑Ω∑® 
 * @author yangtao__anxpp.com 
 * @version 1.0 
 */  
public class TestResult {  
    /**
     * ≤‚ ‘result
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
    	byte[] b = null;
    	Result.write(3, b);
    	Result.write(1, b);
    	Result.write(3, b);
    	Result.write(3, b);
    	Result.write(4, b);
    	Result.write(5, b);
    	Result.write(2, b);
    	Result.write(2, b);
    	Result.write(1, b);
    	Result.write(3, b);
    	
    	b = Result.ask(2);
    }  
}  