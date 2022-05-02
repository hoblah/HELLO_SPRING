package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

class MemoryMemberRepositoryTest {

    // 인터페이스인 MemberRepository 생성.
    // MemberRepository repository = new MemoryMemberRepository();

    // MemoryMemberRepository만 테스트하기 때문에 인터페이스에서 MemoryMemberRepository로 타입 변경함.
    MemoryMemberRepository repository = new MemoryMemberRepository();

    // 테스트 메서드가 끝날때마다 객체를 지우게된다. 메서드 실행순서에 상관없이 오류가 나지않음.
    // 테스틑 서로 의존관계없이 설계가 되어야한다.
    // 그러기 위해선 하나의 테스트가 끝날때마다 저장소나 공용데이터들을 깔끔하게 지워줘야 문제가없다.
    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        // 아이디 꺼내어 result 변수에 대입.
        Member result = repository.findById(member.getId()).get();
        //System.out.println("result = " + (result == member));

        // Assertions > org.junit.jupiter.api 임폴트!
        //Assertions.assertEquals(member, null);

        // Assertions > org.assertj.core.api 임폴트!
        //Assertions.assertThat(member).isEqualTo(result);


        /* import static org.assertj.core.api.Assertions.*;
           임폴트하게 되면 Assertions.을 앞에 안붙혀도 바로 assertThat 함수 사용가능. */
        assertThat(member).isEqualTo(result);
        // 실무에서는 빌드 툴이랑 엮어서 빌드툴에서 빌드할때 테스트에서 통과하지 않으면 다음단계로 못넘어가게 막아버린다.
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        // 복사 후 변수 변경위해 쉬프트 + F6누르면 변경하는대로 해당 변수명이 다 변경됨.
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        // spring1 이름 메서드를 태워 찾아 > get()으로 꺼내어 (get()으로 꺼내면 Optional을 까서 꺼낼수있음) >  result 변수로 대입.
        // Optional<Member> result = repository.findByName("spring1").get();
        Member result = repository.findByName("spring1").get();

        // 찾아서 꺼낸 result가 member1이랑 같은지 확인.
        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        // 복사 후 변수 변경위해 쉬프트 + F6누르면 변경하는대로 해당 변수명이 다 변경됨.
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        // 저장된 모든 목록 함수 태운 후 List의 Member타입인 result 변수생성하여 대입.
        List<Member> result = repository.findAll();

        // result의 사이즈는 2개인지 비교.
        assertThat(result.size()).isEqualTo(2);
    }

}
