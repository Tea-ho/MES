package mes.domain.Repository.product;

import mes.domain.dto.performance.ProductProductionDto;
import mes.domain.entity.product.ProductPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductPlanRepository extends JpaRepository<ProductPlanEntity, Integer> {

    // 1. 제품별 생산실적 쿼리[출력내용: 제품명, 제품생산량, 조건: 생산 승인 완료 / Dto 형태로 바로 반환]
    @Query(value = "SELECT p.productEntity.prodName, p.prodPlanCount " +
            "FROM ProductPlanEntity p " +
            "JOIN p.allowApprovalEntity a " +
            "WHERE a.al_app_whether = true " +
            "GROUP BY p.productEntity.prodName " +
            "ORDER BY p.prodPlanCount desc", nativeQuery = true)
    List<ProductProductionDto> findProductProduction();

}
