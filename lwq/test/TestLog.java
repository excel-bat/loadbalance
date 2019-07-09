package test;

import org.apache.log4j.Logger;

public class TestLog {
	public static void main(String[] args) throws InterruptedException{
		Logger log = Logger.getLogger(TestLog.class);
		for(int i = 0;i<5;i++){
			log.fatal("严重错误信息："+i);
			Thread.sleep(1);
			log.error("错误信息："+i);
			Thread.sleep(2);
			log.warn("警告信息："+i);
			Thread.sleep(4);
			log.info("一般信息："+i);
			Thread.sleep(8);
			log.debug("调试信息："+i);
			Thread.sleep(10);
		}
	}
}
