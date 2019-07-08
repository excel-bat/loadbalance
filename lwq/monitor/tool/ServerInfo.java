package monitor.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import monitor.bio.ClientBio;

public class ServerInfo {
	public int serverAmount;
	public String[] serverIp;
	public int[] serverPort;
    public double[] serverCpuRate;
    public int[] serverIndex;

    
    public void init() {
    	try {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));     
            String str;
            System.out.println("Enter server amount:");
            str = stdin.readLine(); 
            serverAmount = Integer.parseInt(str);            
            serverIp = new String[serverAmount];
            serverPort = new int[serverAmount];
            serverCpuRate = new double[serverAmount];
            serverIndex = new int[serverAmount];
            for (int id = 0; id < serverAmount; id++) {
                System.out.printf("Enter #%d server IP:\n", id);
                str = stdin.readLine(); 
                serverIp[id] = str;
                System.out.printf("Enter #%d server port:\n", id);
                str = stdin.readLine(); 
                serverPort[id] = Integer.parseInt(str);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public void sortCpu() {
    	for (int id = 0; id < serverAmount; id++) {
    		serverIndex[id] = id;
        }
        for (int i = 0; i < serverAmount - 1; i++) {
            for (int j = 0; j < serverAmount - 1 - i; j++){
                if (serverCpuRate[serverIndex[j + 1]] < serverCpuRate[serverIndex[j]]) {
                    int temp = serverIndex[j + 1];
                    serverIndex[j + 1] = serverIndex[j];
                    serverIndex[j] = temp;
                }
            }
        }
    }
    public void renewCpu() {
    	for (int id = 0; id < serverAmount; id++) {
    		serverCpuRate[id] = ClientBio.send(serverIp[id], serverPort[id], "getCPU");
        }
    }
    public void showCpu() {
    	for (int id = 0; id < serverAmount; id++) {
            System.out.printf("#%d cpu rate: %f; ", serverIndex[id], serverCpuRate[serverIndex[id]]);
        }
    	System.out.println();
    }
}
