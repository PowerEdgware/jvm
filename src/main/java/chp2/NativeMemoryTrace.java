package chp2;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import sun.misc.Unsafe;

//-Xms100M -Xmx200m  -XX:MaxDirectMemorySize=20M -XX:NativeMemoryTracking=detail -verbosegc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -showversion
public class NativeMemoryTrace {

	static volatile Unsafe unsafe;

	static Unsafe getUnsafe() throws IllegalArgumentException, IllegalAccessException {
		Field field = Unsafe.class.getDeclaredFields()[0];
		field.setAccessible(true);
		unsafe = (Unsafe) field.get(null);
		return unsafe;
	}

	static final int _1MB = 1024 * 1024;
	static final int small_KB = 10 * 1024;

	public static void main(String[] args) throws Exception {
		Unsafe unsafe = getUnsafe();

//		 useDirectBufferAllocate();
		useUnsafeAllocate();

	}

	// Exception in thread "main" java.lang.OutOfMemoryError
	// at sun.misc.Unsafe.allocateMemory(Native Method)
	static void useUnsafeAllocate() {
		int size = _1MB;
		while (true) {
			long addr = unsafe.allocateMemory(size);
			// 设置内存.物理内存被疯狂占用，直到内存爆掉。如果不设置内存值，会怎么样,实际物理内存不变，增加的只是虚拟内存
			// 不管设置内存还是不设置，最终都会OOM
//			unsafe.setMemory(addr, size, (byte) 12);
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
		}
	}

	/*
	 * Exception in thread "main" java.lang.OutOfMemoryError: Direct buffer
	 * memory at java.nio.Bits.reserveMemory(Bits.java:694) at
	 * java.nio.DirectByteBuffer.<init>(DirectByteBuffer.java:123) at
	 * java.nio.ByteBuffer.allocateDirect(ByteBuffer.java:311)
	 */
	// List Size=3564 -->java.lang.OutOfMemoryError: Direct buffer memory
	static void useDirectBufferAllocate() {
		List<ByteBuffer> bufList = new ArrayList<ByteBuffer>();
		while (true) {
			ByteBuffer buf = ByteBuffer.allocateDirect(small_KB);
			bufList.add(buf.put("abcdefghijklmnopqrstuvwxyz".getBytes()));
			bufList.add(buf.put("abcdefghijklmnopqrstuvwxyz".getBytes()));
			if (bufList.size() % 5 == 0) {
				Random rnd = new Random();
				int rndIndex = rnd.nextInt(bufList.size());
				bufList.remove(rndIndex);
				System.out.println("List Size=" + bufList.size());
			}
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
		}
	}
}
