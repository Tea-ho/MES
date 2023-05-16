package mes.domain.Repository.product;

import mes.domain.dto.performance.ProductProductionDto;
import mes.domain.entity.product.ProductPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductPlanRepository extends JpaRepository<ProductPlanEntity, Integer> {

    // 1. 제품별 생산실적 쿼리 [23.05.16, th]
    // 출력내용: 제품명, 제품 원가, 평균 생산량, 총 생산횟수, 총 생산량, 생산비중, 조건: 생산 승인 완료 / Dto 형태로 바로 반환]
    @Query(value = "SELECT p.prodName, " +
            "CAST(AVG(p.prodPrice) AS DECIMAL) AS prodPrice, " +
            "CAST(SUM(pp.prodPlanCount) / COUNT(pp.prodPlanNo) AS DECIMAL) AS averageProductionCount, " +
            "COUNT(pp.prodPlanNo) AS totalProductionCount, " +
            "CAST(SUM(pp.prodPlanCount) AS DECIMAL) AS totalProductionAmount, " +
            "CAST(SUM(pp.prodPlanCount) / (SELECT SUM(pp2.prodPlanCount) FROM mes.product_plan pp2 WHERE pp2.productEntity.prodId = pp.productEntity.prodId) * 100 AS DECIMAL) AS productionPercentage " +
            "FROM mes.product_plan pp " +
            "JOIN pp.productEntity p " +
            "JOIN pp.allowApprovalEntity aa " +
            "WHERE aa.al_app_whether = true " +
            "GROUP BY p.prodName " +
            "ORDER BY p.prodName ASC", nativeQuery = true)
    List<ProductProductionDto> findProductProduction();
    // 특이사항: nativeQuery 사용[판매실적 쿼리와 다른 접근]
}
