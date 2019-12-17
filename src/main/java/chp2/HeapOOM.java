package chp2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @ClassName: HeapOOM
 * @Description:JAVA对内存溢出测试.OOM->OutOfMemoryError
 * @author shangcj
 * @date 2018年6月30日
 * VM Args: -Xms20M -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {

	public static void main(String[] args) {
		List<OOMObject> list = new ArrayList<>();
		while (true) {
			try {
				
				list.add(new OOMObject());
			}
//			} catch (Error e) {
//				e.printStackTrace();
//				try {
//					TimeUnit.SECONDS.sleep(1);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//			}
			finally {
				
			}
		}
		//TODO
		/**
		 * java.lang.OutOfMemoryError: Java heap space
Dumping heap to java_pid18272.hprof ...
Heap dump file created [27964738 bytes in 0.085 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at java.util.Arrays.copyOf(Arrays.java:3210)
	at java.util.Arrays.copyOf(Arrays.java:3181)
	at java.util.ArrayList.grow(ArrayList.java:261)
	at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:235)
	at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:227)
	at java.util.ArrayList.add(ArrayList.java:458)
	at chp2.HeapOOM.main(HeapOOM.java:19)
		 */
	}

	static class OOMObject {

	}

}
