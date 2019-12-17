package chp2;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

//-Xmx512m -XX:+HeapDumpOnOutOfMemoryError
//-verbosegc  -Xloggc:E:\\gclog%p.log
//-XX:+UseParallelGC 
//-XX:+PrintGCDetails
//-XX:+PrintGCDateStamps 
//-XX:NativeMemoryTracking=detail
//-XX:MaxMetaspaceSize=20M  -XX:MetaspaceSize=10m
//-XX:+UnlockDiagnosticVMOptions  
//-Dsun.reflect.noCaches=true -Dsun.reflect.noInflation=true

public class MetaSpaceOverheadUseReflect {

	public static void main(String[] args) {
		try {
			useReflectCauseMetaSpaceOverflow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * java.lang.OutOfMemoryError: Metaspace Dumping heap to java_pid10680.hprof
	 * ... Heap dump file created [5490846 bytes in 0.028 secs]
	 * 
	 * @throws Exception
	 */
	static void useReflectCauseMetaSpaceOverflow() throws Exception {
		// 一些例子
		// -XX:+TraceClassLoading
		Class<?> manClazz = Class.forName("chp2.Man");
		Object instanceMan = manClazz.newInstance();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			Method speakMethod = manClazz.getMethod("speak", String.class);
			Method workMethod = manClazz.getMethod("work");
			speakMethod.invoke(instanceMan, String.valueOf(i));
			workMethod.invoke(instanceMan);
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(5));
		}
	}
}

class Man {
	public void speak(String word) {

	}

	public void work() {
	}
}
