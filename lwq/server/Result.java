package server;

import java.io.IOException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Result {
	public static String fileName = "data.txt";
	public static byte[] ask(int len) {
		byte[] b = null;
		try {
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			boolean find = false;
			String[] split;
			long lenFile = randomFile.length();  
            long start = randomFile.getFilePointer();  
            long nextend = start + lenFile - 1;  
            String line;  
            if (lenFile == 0) {
            	System.out.println("not find");
            	b = new byte[8];
            	Arrays.fill(b, (byte) 0);
            	b[3] = (byte) 3;
            	randomFile.close();
            	return b;
            }
            randomFile.seek(nextend);  
            int c = -1;  
            while ((nextend > start)) {  
                c = randomFile.read();  
                if (c == '\n' || c == '\r') {  
                    line = randomFile.readLine();  
                    if (line != null) {  
                        //System.out.println(line);  
                        split = line.split(",");
                        //System.out.println(split[0] + " " + split[1] + " " + split[2]);
                        int l = Integer.parseInt(split[0]);
                        if (l == len) {
                        	find = true;
                        	System.out.println(split[0] + " " + split[1] + " " + split[2]);
                        	break;
                        }
                    }
                    nextend--;  
                }  
                nextend--;  
                randomFile.seek(nextend);  
                if (nextend == 0) {// 当文件指针退至文件开始处，输出第一行  
                    // System.out.println(rf.readLine());  
                    line = randomFile.readLine();  
                    if (line != null) {  
                        //System.out.println(line);  
                        split = line.split(",");
                        //System.out.println(split[0] + " " + split[1] + " " + split[2]);
                        int l = Integer.parseInt(split[0]);
                        if (l == len) {
                        	find = true;
                        	//System.out.println(split[0] + " " + split[1] + " " + split[2]);
                        	break;
                        }
                    }
                }  
            } 
            if (find)
            {
            	System.out.println("find");
            	b = new byte[4 + 4 + len];
            	b[0] = b[1] = b[2] = (byte) 0;
            	b[3] = (byte) 3;
            	b[4] = (byte) ((len >> 24) & 0xff);
        		b[5] = (byte) ((len >> 16) & 0xff);
        		b[6] = (byte) ((len >> 8) & 0xff);
        		b[7] = (byte) (len & 0xff);
        		for (int i = 0; i < len; i++) b[i + 8] = (byte) 48;
            	
            } else {
            	System.out.println("not find");
            	b = new byte[8];
            	Arrays.fill(b, (byte) 0);
            	b[3] = (byte) 3;
            }
            randomFile.close();
			
		} catch (Exception e) {
            e.printStackTrace();
        }
		return b;
	}
	public static void write(int len, byte[] data) {
		try {
			Date currentTime = new Date(); 
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss"); 
	        String s8 = formatter.format(currentTime);
			
			byte[] b = new byte[len];
			Arrays.fill(b, (byte) (48));
			String info = new String(b,"UTF-8");
			String content = "" + len + "," + s8 + "," + info + "\n"; 
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
            System.out.print("Write content : " + content);
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static byte[] getWrit() {
		byte[] b = new byte[8];
		Arrays.fill(b, (byte) (0));
		b[3] = (byte) 4;
		return b;
	}
}