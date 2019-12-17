package chp2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

//-Xmx1024m -Xms512m -XX:+HeapDumpOnOutOfMemoryError
//-verbosegc 
//-XX:+UseParallelGC 
//-XX:+PrintGCDetails
//-XX:+PrintGCDateStamps 
//-XX:MaxMetaspaceSize=200M  -XX:MetaspaceSize=128m
//-XX:+UnlockDiagnosticVMOptions  
//-Dsun.reflect.noInflation=true -Dsun.reflect.noCaches=true
public class CglibLoadClass implements MethodInterceptor {

	public Object getProxy(Class clazz) {
		Enhancer enhancer = new Enhancer();
		// 设置需要创建子类的类
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		enhancer.setUseCache(false);
		// 通过字节码技术动态创建子类实例
		return enhancer.create();
	}

	// 实现MethodInterceptor接口方法
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		// System.out.println("前置代理");
		// 通过代理类调用父类中的方法
		Object result = proxy.invokeSuper(obj, args);
		// System.out.println("后置代理");
		return result;
	}

	static int nthread = Runtime.getRuntime().availableProcessors() * 10;
	static ExecutorService es = Executors.newFixedThreadPool(nthread);

	public static void main(String[] args) {
		startGenSayHelloCls();

		// reflect invoke
		try {
			reflectInvoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void reflectInvoke() throws Exception {
		Class<?> manClazz = Class.forName("chp2.SayService");
		Object instanceMan = manClazz.newInstance();

		for (int i=nthread;i-->0;) {
			es.execute(() -> {
				try {
					exec(manClazz, instanceMan);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

	}

	static void exec(Class<?> manClazz, Object instanceMan) throws Exception {
		Method speakMethod = manClazz.getMethod("speak", String.class);
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			long start = System.currentTimeMillis();
			speakMethod.invoke(instanceMan, String.valueOf(i));
			if (i % 200 == 0) {
				System.out.println(Thread.currentThread().getName() + " ->" + i + " cost="
						+ (System.currentTimeMillis() - start) + " ms");
			}
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(5));
		}
	}

	private static void startGenSayHelloCls() {
		new Thread("GenSayHello-thread") {
			public void run() {
				CglibLoadClass cglibLoadClass = new CglibLoadClass();
				while (true) {
					SayService hello = (SayService) cglibLoadClass.getProxy(SayService.class);
					hello.say();
					LockSupport.parkNanos(cglibLoadClass, TimeUnit.MILLISECONDS.toNanos(300));
				}
			};
		}.start();
	}

}

class SayService {
	public void say() {
		// System.out.println("hello everyone");
	}

	public void speak(String word) {
		LockSupport.parkNanos(this, TimeUnit.MILLISECONDS.toNanos(10));
	}
}
