package server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import tools.CpuMonitorCalc;

/**
 * AIO服务端accept回调接口，获得cpu状态，调用write。
 * 
 * @author zhaohr16
 * @date 2019/07/03
 */
public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    @Override
    public void completed(AsynchronousSocketChannel socket, AsynchronousServerSocketChannel serverSocket) {
        try {
            serverSocket.accept(serverSocket, this);
            System.out.println("connected from " + socket.getRemoteAddress().toString());
            // double cpuTotal = SigarCpu.getCpuTotal();
            CpuMonitorCalc.getInstance().getProcessCpu();
            Thread.sleep(100);
            double cpu = CpuMonitorCalc.getInstance().getProcessCpu();
            System.out.println("当前进程cpu占用率: " + cpu);
            ByteBuffer buffer = ByteBuffer.wrap(String.valueOf(cpu).getBytes("UTF-8"));
            socket.write(buffer, socket, new AioWriteHandler(buffer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
        exc.printStackTrace();

    }

}
