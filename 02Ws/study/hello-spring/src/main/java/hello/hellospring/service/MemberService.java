package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MemberService {

    //private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final MemberRepository memberRepository;
    // 컨스트럭터 - 외부에서 넣어주도록 변경. 직접 new해서 생성하는것이 아님.
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    public Long join(Member member) {
        // 같은 이름이 있는 중복 회원X

        /*
        Optional<Member> result = memberRepository.findByName(member.getName());
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }); */

        // 앞에 옵셔널을 지운후 바로 뒤에 메서드를 붙혀 사용가능.
        validateDuplicateMember(member); // 중복 회원 검증.

        memberRepository.save(member);
        return member.getId();
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
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
