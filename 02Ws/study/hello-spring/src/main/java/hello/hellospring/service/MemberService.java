package hello.hellospring.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*  JPA는 join() 메서드를 들어갈때 다 트랜잭션 안에서 실행 되어야 한다.
    @Transactional 어노테이션 추가.
 */
@Transactional
public class MemberService {

    //private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final MemberRepository memberRepository;
    // 컨스트럭터 - 외부에서 넣어주도록 변경. 직접 new해서 생성하는것이 아님.
    @Autowired // 주입.
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    public Long join(Member member) {
        // 같은 이름이 있는 중복 회원X

        // 현재 밀리초 - 천 분의 1초
        long start = System.currentTimeMillis();

        /* 로직이 끝날때 시간을 측정해야한다.
           예외가 터져도 찍어야 하는경우 try{} finally{}을 해야함. */
        try{
            /*
            Optional<Member> result = memberRepository.findByName(member.getName());
            result.ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
            }); */
            // 앞에 옵셔널을 지운후 바로 뒤에 메서드를 붙혀 사용가능.
            validateDuplicateMember(member); // 중복 회원 검증.

            memberRepository.save(member);
            return member.getId();

        } finally {
            // 함수 끝나기전 밀리초 체크.
            long finish = System.currentTimeMillis();
            // 끈날때 시간에서 시작하는 시간 빼기.
            long timeMs = finish - start;
            // 계산한 초 출력.
            System.out.println("join = "+ timeMs + "ms");
        }

    }

    // Refactor This > Ctrl+Alt+Shift+T > 리팩토링 관련 전체 항목을 조회.
    // Extract Method > Ctrl+Alt+M - 메소드 추출 리팩토링은 메소드를 더 짧고 읽기 쉽게 만들 수 있습니다.
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원 조회 > 모든 전체 목록을 표시하는 메서드를 호출하여 리턴함.
     */
    public List<Member> findMembers() {
        long start = System.currentTimeMillis();
        try{
            return memberRepository.findAll();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("join = "+ timeMs + "ms");
        }

    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
