package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class MemberServiceTest {

    //MemberService memberService = new MemberService();
    //MemoryMemberRepository memoryMemberRepository = new MemoryMemberRepository();
    MemberService memberService;
    MemoryMemberRepository  memberRepository;
    
    /* 테스트 실행할때마다 해당 메서드가 실행되며 새로운 객체가 각각 실행된다.
       테스트는 독립적으로 실행되야한다. 각테스트를 실행하기전에
       MemoryMemberRepository()를 생성하고,
       해당 변수를 MemberService의 매개변수로 넣어준다.
       그러면 같은 MemberRepository가 사용이 될것이다.
       이것은 MemberService.java 자바 클래스 입장에서는 직접 new 하지않고,
       외부에서 memberRepository를 넣어준다.
       이것을 디펜더시 인젝션 즉 DI라고 한다.
     */
    @BeforeEach
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    // 테스트 메서드가 끝날때마다 객체를 지우게된다. 메서드 실행순서에 상관없이 오류가 나지않음.
    // 테스틑 서로 의존관계없이 설계가 되어야한다.
    // 그러기 위해선 하나의 테스트가 끝날때마다 저장소나 공용데이터들을 깔끔하게 지워줘야 문제가없다.
    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }
    @Test
    void 회원가입() {

        //given  > 주어진 이름 데이터.
        Member member = new Member();
        member.setName("hello");

        //when> 멤버의 join메서드 검증.
        // 이름을 보내면 해당 연관된 아이디를 반환.
        Long saveId = memberService.join(member);

        //then

        // 우리가 저장한게 리포지토리에 있는게 맞는지 확인위해
        // 리포지토리를 꺼내야함 > saveId 넘기고, get으로 받는다.
        // Optional<Member> findMember = memberService.findOne(saveId).get();
        Member findMember = memberService.findOne(saveId).get();

        // 이름이 저장한 이름과 저장되어있는 이름이 같은지 비교.
        //Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
        // 스테틱 임폴트로 넘김. > import static org.assertj.core.api.Assertions.*; 코드 임포트 추가.
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    /* 회원가입시 핵심은 저장되는것도 중요하지만,
       중복회원검증 로직을 잘태워서 예외가 터지는것도 봐야한다. */
    // 중복 회원 예외 메서드 생성.
    @Test
    public void 중복_회원_예외() {
        //given
        // 일부러 변수2개를 만들어 저장이름을 같도록 spring으로 설정.
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);

        // 람다식 사용, 이 익셉션이 터져야하고 어떤 로직을 태울때 이것을 넣으면 예외가 터져야한다.
        // IllegalStateException이 아닌 NullPointerException.class를 사용하면!! 테스트 실패함.
        // assertThrows(NullPointerException.class, () -> memberService.join(member2));
        // assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        // 메세지 검증 위해 IllegalStateException e = 로 받아 반환함. ctrl + shift + v 하면 자동생성.
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        // 메세지 받아서 메세지가 같은지 검증.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

/*        try {
            // 중복 익셉션 터지면 catch로!
            memberService.join(member2);
            fail(); // 실패!
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다. 123");
        }
*/


        //then
    }
    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}