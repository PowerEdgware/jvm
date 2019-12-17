package chp2;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import sun.misc.Unsafe;

/**
 * 
    * @ClassName: DirectMemoryOOM  
    * @Description:使用unsafe分配本机内存测试本机直接内存溢出
    * @author shangcj 
    * @date 2018年6月30日  
    *-Xmx20M -Xms20M -XX:MaxDirectMemorySize=10M
 */
public class DirectMemoryOOM {

	static final int  _1MB=1024*1024;
	public static void main(String[] args) throws Exception {
		Field field=Unsafe.class.getDeclaredFields()[0];
		field.setAccessible(true);
		
		Unsafe unsafe=(Unsafe) field.get(null);
		while(true){
			try {
				unsafe.allocateMemory(_1MB);
			} catch (Throwable e) {
				System.out.println(e);
			}
		}
		/**
		 * Exception in thread "main" java.lang.OutOfMemoryError
	at sun.misc.Unsafe.allocateMemory(Native Method)
	at chp2.DirectMemoryOOM.main(DirectMemoryOOM.java:24)
		 */
		//DirectByteBuffer
		//ByteBuffer
	}
}
