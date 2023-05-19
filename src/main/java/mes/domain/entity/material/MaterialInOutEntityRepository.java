package mes.domain.entity.material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialInOutEntityRepository extends JpaRepository<MaterialInOutEntity , Integer> {


    @Query(value = "select * from material_in_out where matid=:matID" , nativeQuery = true)
    Page<MaterialInOutEntity> findByMatid(int matID, Pageable pageable);

    @Query(value = "select * from material_in_out where matid=:matID" , nativeQuery = true)
    List<MaterialInOutEntity> findByMid(int matID);

    @Query(value = "select * from material_in_out where matid=:matID and mat_in_code = 1" , nativeQuery = true)
    List<MaterialInOutEntity> findByMaterial(int matID);

    @Query(value = "select * from material_in_out where matid=:matID and 1=mat_in_code order by udate DESC limit 1"  , nativeQuery = true)
    Optional<MaterialInOutEntity> findByUdate(int matID);

    @Query(value = "select * from material_in_out where al_app_no=:al_app_no"  , nativeQuery = true)
    MaterialInOutEntity findByAlid(int al_app_no);

    @Query(value = "select date_format(udate ,'%Y-%m-%d') from material_in_out where matid=:matID and 1=mat_in_code order by udate asc limit 1;"  , nativeQuery = true)
    MaterialInOutEntity findByFirstDate(int matID);

    @Query(value = "select date_format(udate ,'%Y-%m-%d') from material_in_out where matid=:matID and 1=mat_in_code order by udate desc limit 1;"  , nativeQuery = true)
    MaterialInOutEntity findByLastDate(int matID);
}
