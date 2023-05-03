package mes.service.member;

import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberSerivce {

    @Autowired private MemberRepository memberRepository;

    public boolean login(String username, String password) {
        MemberEntity member = memberRepository.findByUsernameAndPassword(username, password);
        return member != null;
    }

}

