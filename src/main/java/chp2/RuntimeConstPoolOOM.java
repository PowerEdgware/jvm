package chp2;

import java.util.ArrayList;
import java.util.List;

/**
 * 
    * @ClassName: RuntimeConstPoolOOM  
    * @Description:
    * @author shangcj 
    * @date 2018年6月30日  
    *-XX:PermSize=10M -XX:MaxPermSize=10M
 */
public class RuntimeConstPoolOOM {

	
	public static void main(String[] args) {
		String str1=new StringBuilder("计算").append("数据").toString();
		System.out.println(str1.intern()==str1);
		
		String str2=new StringBuilder("ja").append("va").toString();
		System.out.println(str2.intern()==str2);
		
		List<String> list=new ArrayList<>();
		int i=0;
		while(true){
			list.add(String.valueOf(i++).intern());
		}
	}
}
