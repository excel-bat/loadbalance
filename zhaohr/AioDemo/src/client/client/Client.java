package client;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import data.ServerInfo;
import tools.WorkThreadPool;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class Client {

    public static void main(String[] args) {
        ServerInfo.setServerList();
        try {
            ExecutorService executor = WorkThreadPool.newFixedThreadPool(2);
            executor.execute(new StrategyInfoClient());
            executor.execute(new FileClient());
            int i = 0;
            while (true) {
                System.out.print(i);
                for (ServerInfo serverInfo : ServerInfo.serverList) {
                    System.out.print("  +++" + serverInfo.connectCount + "         ");
                }
                System.out.println();
                Thread.sleep(5000);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
