package monitor.bio;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.net.Socket;  

/**
 * ClientBio class
 * 
 * @author LiWeiqi
 * @date 2019/07/02
 */
public class ClientBio {  
    private static int server_amount = 1000;  
    private static String[] server_ip;
    private static int[] server_port;
    private static double[] server_cpu_rate;
    private static int[] sorted_index;
    public static void main(String[] strings) {
        try {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));     
            String str;
            System.out.println("Enter server amount:");
            str = stdin.readLine(); 
            server_amount = Integer.parseInt(str);            
            server_ip = new String[server_amount];
            server_port = new int[server_amount];
            server_cpu_rate = new double[server_amount];
            sorted_index = new int[server_amount];
            for (int id = 0; id < server_amount; id++) {
                System.out.printf("Enter #%d server IP:\n", id);
                str = stdin.readLine(); 
                server_ip[id] = str;
                System.out.printf("Enter #%d server port:\n", id);
                str = stdin.readLine(); 
                server_port[id] = Integer.parseInt(str);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        while (true) {
            System.out.println("------------------------");
            for (int id = 0; id < server_amount; id++) {
                server_cpu_rate[id] = send(server_ip[id], server_port[id], "getCPU");
            }
            for (int id = 0; id < server_amount; id++) {
                sorted_index[id] = id;
            }
            for (int i = 0; i < server_amount - 1; i++) {
                for (int j = 0; j < server_amount - 1 - i; j++){
                    if (server_cpu_rate[sorted_index[j + 1]] < server_cpu_rate[sorted_index[j]]) {
                        int temp = sorted_index[j + 1];
                        sorted_index[j + 1] = sorted_index[j];
                        sorted_index[j] = temp;
                    }
                }
            }
            for (int id = 0; id < server_amount; id++) {
                System.out.printf("#%d cpu rate: %f; ", sorted_index[id], server_cpu_rate[sorted_index[id]]);
            }
            System.out.println();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static double send(String sip, int port,String expression){  
        //System.out.println("  getCPU of " + port);  
        Socket socket = null;  
        BufferedReader in = null;  
        PrintWriter out = null;  
        double rate = 1.0;
        try{  
            socket = new Socket(sip,port);  
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            out = new PrintWriter(socket.getOutputStream(),true);  
            out.println(expression);  
            rate = Double.parseDouble(in.readLine());
            
            //System.out.println("  receive cpu rate: " + in.readLine());  
        }catch(Exception e){  
            System.out.println("  getCPU of " + port + " failed "); 
            //e.printStackTrace();  
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
        return rate;
    }  
}  