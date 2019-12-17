package clazz;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Test {

	static boolean stop=false;
	public static void main(String[] args) throws IOException {
		
		try {
			Class.forName("abc");
//			ConcurrentHashMap<K, V>
			//Test.class.getDeclaredField("aa");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Thread t=new Thread(()->{
			while(!stop){
				System.out.println("aaa");
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stop=true;
//		System.in.read();
		
	}
}
