package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTraceAop {

    /* 해당 어노테이션 뒤에붙는 해석은
	    execution 다음
		hello.hellospring 패키지명
		그 밑에 있는 * 등등 해서 원하는 조건을
		넣을 수 가 있다.
		지금은 hello.hellospring 패키지 하위는
		다 적용 시키는 것으로 설정하였다.
		* 부분에 클래서 명도 넣을 수 있다. */
    @Around("execution(* hello.hellospring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 밀리초 받기.
        long start = System.currentTimeMillis();
        // joinPoint에 어떤 메서드가 호출됬는지 이름을 얻을수 있음.
        System.out.println("START: " + joinPoint.toString());

        try {
            // joinPoint.proceed();를 하면 다음메서드로 진행된다.
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() +" " + timeMs + "ms");
        }

    }
}
