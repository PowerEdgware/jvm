package chp2;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

//-Xms220M -Xmx220m -verbosegc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError 
//-XX:NativeMemoryTracking=summary -XX:MetaspaceSize=20m -XX:MaxMetaspaceSize=20m
public class MetaSpaceTrace {

	public static void main(String[] args) {
		// ClassLoader ProxyClassFactory
		// sun.misc.Launcher$AppClassLoader@73d16e93
//		System.out.println(MetaACollector.class.getClassLoader());
//		System.out.println(MetaACollector.class.getName());
		useURLClassLoader();
	}

	//TODO metaspace:20m,maxmetaspace=20m no sleep
	// java.lang.OutOfMemoryError: GC overhead limit exceeded
	// Dumping heap to java_pid1284.hprof ...
	// Heap dump file created [364033784 bytes in 1.708 secs]
	// Exception in thread "main" [Full GC (Ergonomics)
	// java.lang.OutOfMemoryError: GC overhead limit exceeded
	//TODO with sleep  -XX:MetaspaceSize=6m -XX:MaxMetaspaceSize=9m
	//java.lang.OutOfMemoryError: Compressed class space
	//Dumping heap to java_pid13208.hprof ...
	//Heap dump file created [4909884 bytes in 0.019 secs]
	
	//TODO 调大  -XX:MetaspaceSize=6m -XX:MaxMetaspaceSize=10m
	private static void useURLClassLoader() {
		URL url = null;
		List<ClassLoader> classLoaderList = new ArrayList<ClassLoader>();
		try {
			url = new File("e:\\u\\chp2").toURI().toURL();
			URL[] urls = { url };
			while (true) {
				ClassLoader loader = new URLClassLoader(urls);
				classLoaderList.add(loader);
				loader.loadClass("chp2.MetaACollector");
				
				//加上休眠，让MetaSpace缓慢增长
				LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(50));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 好像没用
	private static void genClassMeta() {
		final MetaACollector target = new MetaACollector();
		Class<?> clazz = MetaACollector.class;
		List<Object> proxyList = new ArrayList<>();
		while (true) {
			Object proxyA = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(),
					new InvocationHandler() {
						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							return method.invoke(target, args);
						}
					});
			proxyList.add(proxyA);
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
			System.out.println(proxyList.size());
		}
	}
}

interface MetaCollector {
	Object getMeta(Class<?> clazz);
}

class MetaACollector implements MetaCollector {

	@Override
	public Object getMeta(Class<?> clazz) {
		return clazz.getSimpleName();
	}

}
