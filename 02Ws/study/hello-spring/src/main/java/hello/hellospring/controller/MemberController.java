package hello.hellospring.controller;
import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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

    @GetMapping("/members/new")
    public String createForm(){
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form){
        // Member 임폴트시 hello.hellospring.domin인 우리가 만든 코드로 임포트!
        Member member = new Member();
        // 사용자가 입력한 값인 매개변수로 받은 form의 name을 새로운 member객체의 name에 세팅.
        member.setName(form.getName());

        // 입력된 이름 출력
        System.out.println("member = " + member.getName());

        // 이름이 저장된 member객체를 join메서드를 태워 회원등록실행.
        memberService.join(member);

        // 홈화면으로 보냄.
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        // 모든 멤버를 꺼내온다.
        List<Member> members = memberService.findMembers();
        // 가져온 모든 멤버를 목록으로 표시위해 model에 저장함.
        model.addAttribute("members", members);
        // templates 의 화면으로 리턴.
        return "members/memberList";
    }


}
