package mes.domain.entity.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowApprovalRepository extends JpaRepository<AllowApprovalEntity, Integer> {
}
