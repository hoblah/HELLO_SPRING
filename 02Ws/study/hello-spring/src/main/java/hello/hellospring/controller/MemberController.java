package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {

    // 스프링컨테이너에 등록하여 가져다 사용함.
    //private final MemberService memberService;

    private MemberService memberService;

    // 생성자
/*    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }*/

    @Autowired
    public void setMemberService(MemberService memberService){
        this.memberService = memberService;
    }
}
