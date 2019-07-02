package client;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;

import tools.CPUMonitorCalc;
import tools.SigarCpu;

public class AioConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel>{
	@Override
	public void completed(Void result, AsynchronousSocketChannel socket) {
		try {
			//double cpuTotal = SigarCpu.getCpuTotal();
			//double cpu = 0;
			
			CPUMonitorCalc.getInstance().getProcessCpu();
			Thread.sleep(100);
			double cpu = CPUMonitorCalc.getInstance().getProcessCpu();
			
			System.out.println("当前进程CPU占用率: "+cpu);
			ByteBuffer buffer = ByteBuffer.wrap(String.valueOf(cpu).getBytes("UTF-8"));
			socket.write(buffer, socket, new AioWriteBuffer(buffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
		exc.printStackTrace();
		
	}

}
