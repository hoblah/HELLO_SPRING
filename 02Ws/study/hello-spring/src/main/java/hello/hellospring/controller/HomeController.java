package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // > "/" 는 도메인 첫번째인 locathost:8080으로 들어오면 이것이 호출됨.
    @GetMapping("/")
    public String home(){
        // home.html 이 호출된다.
        return "home";
    }
}
