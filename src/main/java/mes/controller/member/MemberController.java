package mes.controller.member;

import lombok.extern.slf4j.Slf4j;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.MemberRepository;
import mes.service.member.MemberSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/home")
public class MemberController {
    @Autowired MemberSerivce memberService;
    @Autowired MemberRepository memberRepository;

    @GetMapping("/login")
    public boolean login(@RequestParam String mname, @RequestParam String password, HttpSession session) {
        boolean result = memberService.login(mname, password);
        if (result) {
            MemberEntity member = memberRepository.findByMnameAndMpassword(mname, password);
            session.setAttribute("member", member);
            return true;
        } else {
            return false;
        }
    }
    @GetMapping("/logout")
    public void logout(HttpSession session) {
        memberService.logout(session);
    }
}
