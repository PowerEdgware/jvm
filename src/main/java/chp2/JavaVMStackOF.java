package chp2;

/**
 * 
    * @ClassName: JavaVMStackOF  
    * @Description:虚拟机栈和本地方法栈溢出
    * @author shangcj 
    * @date 2018年6月30日  
    *VM Args:-Xss128K
 */
public class JavaVMStackOF {
	
	public JavaVMStackOF(){
		
		super();
		//System.gc();
	}

	int stackLen=1;
	public void stackLeak(){
		stackLen++;
		stackLeak();
	}
	
	public static void main(String[] args) throws Throwable {
		JavaVMStackOF oom=new JavaVMStackOF();
		try {
			oom.stackLeak();
		} catch (Throwable e) {
			System.out.println("stack Len="+oom.stackLen);
			throw e;
		}
		/**
		 * Exception in thread "main" stack Len=1001
java.lang.StackOverflowError
	at chp2.JavaVMStackOF.stackLeak(JavaVMStackOF.java:15)
	at chp2.JavaVMStackOF.stackLeak(JavaVMStackOF.java:16)
		 */
	}
}
