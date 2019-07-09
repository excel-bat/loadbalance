package server;

/**
 * Counter class
 * 
 * @author LiWeiqi
 * @date 2019/07/04
 */
public class Counter implements Runnable {
	int stime;
	int counter;
	public Counter() {
		stime = 3;
		counter = 0;
	}
	public Counter(int i) {
		stime = i;
		counter = 0;
	}
	@Override
	public void run() {
		while(true) {
			try {
				System.out.println("counter = " + counter);
				RunServer.mylog.logCounter(counter);
				Thread.sleep(1000 * stime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void addd() {
		counter += 1;
	}
	public void addd(int i) {
		counter += i;
	}
}