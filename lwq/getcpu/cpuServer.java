
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class cpuServer {
    private static int DEFAULT_PORT = 12345;
    private static ServerSocket server;
    public static void main(String[] strings) throws IOException{
        if(server != null) return;
        try{
            server = new ServerSocket(DEFAULT_PORT);
            System.out.println("Setup at port: " + DEFAULT_PORT);
            while(true){
                Socket socket = server.accept();
                new Thread(new ServerHandler(socket)).start();
            }
        }finally{
            if(server != null){
                System.out.println("Closed");
                server.close();
                server = null;
            }
        }
    }
}

class ServerHandler implements Runnable{
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
            String result;
            while(true){
                if((expression = in.readLine())==null) break;
                System.out.println("Received: " + expression);
                sigarwin sigarcpu = new sigarwin();
                out.println(sigarcpu.getCPU());
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