package monitor.bio;

import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import monitor.tool.SigarCpuInfo;

/**
 * ServerHandle class
 * 
 * @author LiWeiqi
 * @date 2019/07/02
 */
public class ServerHandler implements Runnable{
    private Socket socket;
    public ServerHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            String expression;
            SigarCpuInfo sigarcpu = new SigarCpuInfo();
            while(true){
                if((expression = in.readLine())==null) {
                	break;
                }
                System.out.println("Received: " + expression);
                if ("getCPU".equals(expression)) {
                	out.println(sigarcpu.getCpu());                	
                }
                if ("getCoreNum".equals(expression)) {
                	out.println(sigarcpu.getCoreNum());                	
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if(out != null){
                out.close();
                out = null;
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}