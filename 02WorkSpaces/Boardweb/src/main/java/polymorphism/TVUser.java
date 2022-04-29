package polymorphism;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class TVUser {
	 
	public static void main(String[] args) {

	
		// 1. Spring 컨테이너를 구동한다.
		AbstractApplicationContext factory = 
				new GenericXmlApplicationContext("applicationContext.xml");
		
		// 2. Spring 컨테이너로부터 필요한 객체를 요청(Lookup)한다.
		TV tv = (TV)factory.getBean("tv");
		TV tv1 = (TV)factory.getBean("tv");
		TV tv2 = (TV)factory.getBean("tv");
		TV tv3 = (TV)factory.getBean("tv");

		tv.powerOn(); 
		tv.volumeUp();
		tv.volumeDown();
		tv.powerOff();
		
		// 3. Spring 컨테이너를 종료한다.
		factory.close();

	
		
/*		
 		하나만 생성돼도 상관없는 객체들은 
 		객체를 생성하고 주소를 복사하여 재사용 하는것이다.
 		하지만 이렇게 개발을 하는것은 거의 불가능에 가깝다.
		TV tv1 = new SamsungTV();
		TV tv2 = tv1;
		TV tv3 = tv2;
*/		

		
		
		
		
	}
	
}
 
 