package chp2;

/**
 * 
    * @ClassName: JavaVMStackOOM  
    * @Description:创建线程导致内存溢出。（虚拟机栈内存溢出）
    * @author shangcj 
    * @date 2018年6月30日  
    *
 */
public class JavaVMStackOOM {

	private void doRun() {
		while(true){
			try {
				Thread.sleep(100*10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void stackLeakByThread(){
		while(true){
			Thread t=new Thread(){
				public void run() {
					doRun();
				}
			};
			t.start();
		}
	}
	
	public static void main(String[] args) {
		JavaVMStackOOM oom=new JavaVMStackOOM();
		try {
			oom.stackLeakByThread();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		/**
		 * java.lang.OutOfMemoryError: unable to create new native thread
		 */
	}
}
