package test;

import org.apache.log4j.Logger;

public class TestLog {
	public static void main(String[] args) throws InterruptedException{
		Logger log = Logger.getLogger(TestLog.class);
		for(int i = 0;i<5;i++){
			log.fatal("���ش�����Ϣ��"+i);
			Thread.sleep(1);
			log.error("������Ϣ��"+i);
			Thread.sleep(2);
			log.warn("������Ϣ��"+i);
			Thread.sleep(4);
			log.info("һ����Ϣ��"+i);
			Thread.sleep(8);
			log.debug("������Ϣ��"+i);
			Thread.sleep(10);
		}
	}
}
