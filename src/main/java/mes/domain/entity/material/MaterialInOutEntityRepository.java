package mes.domain.entity.material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialInOutEntityRepository extends JpaRepository<MaterialInOutEntity , Integer> {
    @Query(value = "select sum(mat_in_type) from material_in_out" , nativeQuery = true)
    MaterialInOutEntity SumByTpye();

    @Query(value = "select * from material_in_out where matid=:matID" , nativeQuery = true)
    List<MaterialInOutEntity> findByMatid(int matID);


}
