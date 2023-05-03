package mes.domain.entity.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository< MemberEntity, Integer > {

    MemberEntity findByUsernameAndPassword(String username, String password);
}
