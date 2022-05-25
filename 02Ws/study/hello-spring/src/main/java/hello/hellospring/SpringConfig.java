package hello.hellospring;

import hello.hellospring.repository.*;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    // @Autowired DataSource dataSource;

    // DataSource 변수 설정.
/*    private DataSource dataSource;

    // DataSource 생성자 메서드로 빈 주입.
    @Autowired
    public SpringConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }*/

    // JPA 사용위해 EntityManager 를 받아야함.
    // @PersistenceContext
    private EntityManager em;
    @Autowired
    public SpringConfig(EntityManager em){
        this.em = em;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        //return new MemoryMemberRepository();

        /* 스프링 빈 등록을 MemoryMemberRepository() 에서
           > JdbcMemberRepository()로 변경하고
             dataSource는 스프링이 제공해준다. */
        //return new JdbcMemberRepository(dataSource);
        //return new JdbcTemplateMemberRepository(dataSource);

        // JPA 사용위해 EntityManager 를 받아야함.
        return new JpaMemerRepository(em);
    }
}
