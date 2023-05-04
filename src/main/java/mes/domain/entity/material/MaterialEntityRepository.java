package mes.domain.entity.material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialEntityRepository extends JpaRepository<MaterialEntity , Integer> {

}
