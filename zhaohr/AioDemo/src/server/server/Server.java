package server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;

import tools.WorkThreadPool;

/**
 * @author zhaohr16
 * @date 2019/07/10
 */
public class Server {

    private static final String IP = "127.0.0.1";
    private static final int INFOPORT = 8001;
    private static final int FILEPORT = 8000;
    private static Logger logger = Logger.getLogger(Server.class);

    public static void main(String[] args) {
        try {
            ExecutorService executor = WorkThreadPool.newFixedThreadPool(3);
            executor.execute(new StrategyInfo());
            executor.execute(new StrategyInfoServer(IP, INFOPORT));
            executor.execute(new FileServer(IP, FILEPORT));
            while (true) {
                logger
                    .info("服务端接收请求数： " + StrategyInfo.getConnectCountTotal() + "  当前cpu占用率： " + StrategyInfo.getCpu());
                Thread.sleep(3000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
