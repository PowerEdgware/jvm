package chp2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class ThreadIncOOM {

	public static void main(String[] args) {
		
		CounterThread();
		
	unlimitThread();
	}

	private static void CounterThread() {
		new Thread("counter-0"){
			@Override
			public void run() {
				while(true){
					System.out.println("Actived Thread:"+Thread.activeCount());
					LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
				}
			}
		}.start();
	}

	private static void unlimitThread() {
		int i=0;
		for(;;){
			Thread t=new Thread("threadoom-"+(++i)){
				public void run() {
					while(true){
						LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
					}
				};
			};
			t.start();
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(200));
		}
	}
}
