package monitor.bio;

import java.io.IOException;
import java.net.Socket;

import com.sun.management.OperatingSystemMXBean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

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
        OperatingSystemMXBean osBean = null;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
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
                if ("getLoad".equals(expression)) {
                	double load = osBean.getSystemCpuLoad();
                	System.out.println("load : " + load);
                	out.println(osBean.getSystemCpuLoad());
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