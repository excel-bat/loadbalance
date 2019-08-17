package test;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;

public class TestLoad {
	public static void main(String[] args) throws InterruptedException{
		OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);
		while (true) {
			System.out.println(osBean.getSystemCpuLoad());
			Thread.sleep(200);
		}
	}
}
