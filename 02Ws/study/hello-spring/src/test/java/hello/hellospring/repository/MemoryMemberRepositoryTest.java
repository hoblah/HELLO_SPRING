package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.Test;

public class MemoryMemberRepositoryTest {

    MemoryMemberRepositoryTest repository = new MemoryMemberRepositoryTest();

    @Test
    public void save() {
        //given
        Member member = new Member();
        member.setName("spring");
        //when
        //then

    }


}
