package server;

import org.apache.log4j.Logger;

public class LogMaker {
	private Logger log;
	private Logger logCounter;
	public LogMaker() {
		log = Logger.getLogger("thirdLogger");
		logCounter = Logger.getLogger("secondLogger");
	}
	public void logReq() {
		
	}
	public void logStart() {
		log.info("Server start");
		logCounter.info("Server start");
	}
	public void logServer(String str) {
		log.info(str);
	}
	public void logCounter(String str) {
		logCounter.info(str);
	}
	public void logCounter(int counter) {
		logCounter.info("Counter = " + counter);
	}
	public void logDebug(String str) {
		log.debug(str);
	}
	public void logInfo(String str) {
		log.info(str);
	}
	public void logWarn(String str) {
		log.warn(str);
	}
	public void logError(String str) {
		log.error(str);
	}
}
