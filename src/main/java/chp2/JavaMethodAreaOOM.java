package chp2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @ClassName: JavaMethodAreaOOM
 * @Description:-Xmx50M -Xms50M -XX:PermSize10M -XX:MaxPermSize10M
 * @author shangcj
 * @date 2018年6月30日
 *
 *       Java HotSpot(TM) 64-Bit Server VM warning: ignoring option
 *       PermSize=10M; support was removed in 8.0 Java HotSpot(TM) 64-Bit Server
 *       VM warning: ignoring option MaxPermSize=10M; support was removed in 8.0
 */
public class JavaMethodAreaOOM {

	static ExecutorService m = Executors.newFixedThreadPool(10);

	public static void main(String[] args) {
		int x = 2;
		while (++x > 2) {
			AA target = new AA() {
				@Override
				public void add(Object e) {
					m.execute(new Runnable() {
						@Override
						public void run() {
							while (true) {
								try {
									Thread.sleep(10000 * 100);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
				}
			};
//			AA a = (AA) Proxy.newProxyInstance(AA.class.getClassLoader(), new Class[] { AA.class },
//					new InvocationHandler() {
//						@Override
//						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//							System.out.println(proxy.getClass());
//							return method.invoke(target, args);
//						}
//					});
//			 a.add("");
		}
	}

}

interface AA<E> {
	void add(E e);
}
