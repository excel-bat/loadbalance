package monitor.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.AsyncClientHandler;
import monitor.bio.ClientBio;

/**
 * ServerInfo class
 * 
 * @author LiWeiqi
 * @date 2019/07/05
 */
public class ServerInfo {
	public int serverAmount;
	public String[] serverIp;
	public int[] serverPort;
	public int[] monitorPort;
    public double[] serverCpuRate;
    public int[] serverIndex;
    public int[] serverStatus;
    public int[] coreNum;
    public long[] startTime;
    public long[] endTime;
    public long[] useTime;
    public long[] lastTime;
    public double[] aveTime;
    public double[] lasTime;
    public double[] rxbps;
    public double[] txbps;
    public double[] loads;
    public double[] aveLoad;
    public int counter;
    public double[] tp;
    public double[] lastp;
    public double[] tps;
    public long sttime;
    public long edtime;
    public int[] err;
    public int[] errTemp;
    public int[] errTotal;
    public double[] score1;
    public double[] score2;
    public double[] score3;
    
    void makeSpace() {
        serverIp = new String[serverAmount];
        serverPort = new int[serverAmount];
        monitorPort = new int[serverAmount];
        serverCpuRate = new double[serverAmount];
        serverIndex = new int[serverAmount];
        serverStatus = new int[serverAmount];
        coreNum = new int[serverAmount];
        startTime = new long[serverAmount];
        endTime = new long[serverAmount];
        useTime = new long[serverAmount];
        lastTime = new long[serverAmount];
        rxbps = new double[serverAmount];
        txbps = new double[serverAmount];
        loads = new double[serverAmount];
        aveLoad = new double[serverAmount];
        tp = new double[serverAmount];
        tps = new double[serverAmount];
        lastp = new double[serverAmount];
        aveTime = new double[serverAmount];
        lasTime = new double[serverAmount];
        err = new int[serverAmount];
        errTemp = new int[serverAmount];
        errTotal = new int[serverAmount];
        score1 = new double[serverAmount];
        score2 = new double[serverAmount];
        score3 = new double[serverAmount];
        for (int i = 0; i < serverAmount; i++) {
        	score1[i] = 1;
        	score2[i] = 1;
        	score3[i] = 1;
        	aveLoad[i] = 0;
        	aveTime[i] = 10;
        	lasTime[i] = 10;
        	lastp[i] = 0.0;
        	tp[i] = 0.0;
        	tps[i] = 0.0;
        	err[i] = 0;
        	errTemp[i] = 0;
        	errTotal[i] = 0;
        }
        counter = 0;
    }
    
    public void init() {
    	try {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));     
            String str;
            System.out.println("Enter server amount:");
            str = stdin.readLine(); 
            serverAmount = Integer.parseInt(str);  
            makeSpace();
            for (int id = 0; id < serverAmount; id++) {
            	serverStatus[id] = 0;
                System.out.printf("Enter #%d server IP:\n", id);
                str = stdin.readLine(); 
                serverIp[id] = str;
                System.out.printf("Enter #%d server monitor port:\n", id);
                str = stdin.readLine(); 
                monitorPort[id] = Integer.parseInt(str);
                System.out.printf("Enter #%d server port:\n", id);
                str = stdin.readLine(); 
                serverPort[id] = Integer.parseInt(str);
                
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public void init0() {
            serverAmount = 3;            
            makeSpace();

            serverIp[0] = "192.168.210.150";
            serverIp[1] = "192.168.210.151";
            serverIp[2] = "192.168.210.149";
            serverPort[0] = 3000;
            serverPort[1] = 3001;
            serverPort[2] = 3002;
            monitorPort[0] = 2000;
            monitorPort[1] = 2001;
            monitorPort[2] = 2002;
            serverStatus[0] = 0;
            serverStatus[1] = 0;
            serverStatus[2] = 0;
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
    		serverCpuRate[id] = ClientBio.sendCpuRate(serverIp[id], monitorPort[id], "getCPU");
        }
    }
    public void showCpu() {
    	for (int id = 0; id < serverAmount; id++) {
            //System.out.printf("#%d cpu rate: %f; ", serverIndex[id], serverCpuRate[serverIndex[id]]);
            System.out.printf("#%d cpu rate: %f; ", id, serverCpuRate[id]);
        }
    	System.out.println();
    }
    public void getCore() {
    	for (int id = 0; id < serverAmount; id++) {
    		coreNum[id] = ClientBio.sendCoreNum(serverIp[id], monitorPort[id], "getCoreNum");
    	}
    }
    public void renewLoad() {
    	counter++;
    	for (int id = 0; id < serverAmount; id++) {
    		loads[id] = ClientBio.sendCpuRate(serverIp[id], monitorPort[id], "getLoad");
    		aveLoad[id] = ((counter - 1) * aveLoad[id] + loads[id]) / counter;
        }
    }
    public void showLoad() {
    	for (int id = 0; id < serverAmount; id++) {
            //System.out.printf("#%d cpu rate: %f; ", serverIndex[id], serverCpuRate[serverIndex[id]]);
            System.out.printf("#%d load: %f; ", id, loads[id]);
        }
    	System.out.println();
    }
    
    public void showAveLoad() {
    	for (int id = 0; id < serverAmount; id++) {
            //System.out.printf("#%d cpu rate: %f; ", serverIndex[id], serverCpuRate[serverIndex[id]]);
            System.out.printf("#%d ave load: %f; ", id, aveLoad[id]);
        }
    	System.out.println();
    }
    public void showTpsAve() {
    	edtime = System.currentTimeMillis();
    	for (int id = 0; id < serverAmount; id++) {
    		System.out.printf("#%d tps ave: %f; ", id, tp[id] * 1000 / (edtime - sttime));
    	}
    	System.out.println();
    }
    public void showTps() {
    	for (int id = 0; id < serverAmount; id++) {
    		System.out.printf("#%d tps: %f; ", id, tps[id]);
    	}
    	System.out.println();
    }
    public void calcTps() {
    	for (int id = 0; id < serverAmount; id++) {
    		tps[id] = tp[id] - lastp[id];
    		lastp[id] = tp[id];
    	}
    }
    public void showTp() {
    	edtime = System.currentTimeMillis();
    	for (int id = 0; id < serverAmount; id++) {
    		System.out.printf("#%d tp: %f; ", id, tp[id]);
    	}
    	System.out.println();
    }
    public void showTpsComp() {
    	for (int id = 0; id < serverAmount; id++) {
    		System.out.printf("#%d tps complete: %f; ", id, (tps[id] - errTemp[id]));
    	}
    	System.out.println();
    }
    public void showTpComp() {
    	edtime = System.currentTimeMillis();
    	for (int id = 0; id < serverAmount; id++) {
    		System.out.printf("#%d tp complete: %f; ", id, (tp[id] - errTotal[id]));
    	}
    	System.out.println();
    }
    public void showErr() {
    	edtime = System.currentTimeMillis();
    	for (int id = 0; id < serverAmount; id++) {
    		System.out.printf("#%d err: %d; ", id, errTemp[id]);
    	}
    	System.out.println();
    }
    public void showErrTotal() {
    	edtime = System.currentTimeMillis();
    	for (int id = 0; id < serverAmount; id++) {
    		System.out.printf("#%d errTotal: %d; ", id, errTotal[id]);
    	}
    	System.out.println();
    }
    public void clearErr() {
    	for (int id = 0; id < serverAmount; id++) {
    		err[id] = errTemp[id];
    		errTemp[id] = 0;
    	}
    }
    public void showScore1() {
    	edtime = System.currentTimeMillis();
    	for (int id = 0; id < serverAmount; id++) {
    		System.out.printf("#%d Score1: %f; ", id, score1[id]);
    	}
    	System.out.println();
    }
    public void showScore2() {
    	edtime = System.currentTimeMillis();
    	for (int id = 0; id < serverAmount; id++) {
    		System.out.printf("#%d Score2: %f; ", id, score2[id]);
    	}
    	System.out.println();
    }
    public void showScore3() {
    	edtime = System.currentTimeMillis();
    	for (int id = 0; id < serverAmount; id++) {
    		System.out.printf("#%d Score3: %f; ", id, score3[id]);
    	}
    	System.out.println();
    }
    public void runMonitor(int id) {
    	CpuMonitor cpuMonitor = new CpuMonitor(this, id);  
        new Thread(cpuMonitor).start();  
    }
}
