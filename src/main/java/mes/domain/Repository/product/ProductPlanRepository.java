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
/*    @Query(value = "SELECT p.prodName, " +
            "CAST(AVG(p.prodPrice) AS DECIMAL) AS prodPrice, " +
            "CAST(SUM(pp.prodPlanCount) / COUNT(pp.prodPlanNo) AS DECIMAL) AS averageProductionCount, " +
            "COUNT(pp.prodPlanNo) AS totalProductionCount, " +
            "CAST(SUM(pp.prodPlanCount) AS DECIMAL) AS totalProductionAmount, " +
            "CAST(SUM(pp.prodPlanCount) / (SELECT SUM(pp2.prodPlanCount) FROM ProductPlanEntity pp2 WHERE pp2.productEntity.prodId = pp.productEntity.prodId) * 100 AS DECIMAL) AS productionPercentage " +
            "FROM ProductPlanEntity pp " +
            "JOIN pp.productEntity p " +
            "JOIN pp.allowApprovalEntity aa " +
            "WHERE aa.al_app_whether = true " +
            "GROUP BY p.prodName " +
            "ORDER BY p.prodName ASC", nativeQuery = true)*/
    @Query(value = "SELECT new mes.domain.dto.performance.ProductProductionDto(p.prodName, " +
            "CAST(p.prodPrice AS int) AS prodPrice , " +
            "CAST(SUM(CAST(pp.prodPlanCount AS int)) / COUNT(pp.prodPlanNo) AS long) AS averageProductionCount, " +
            "CAST(COUNT(pp.prodPlanNo) AS int) AS totalProductionCount, " +
            "CAST(SUM(CAST(pp.prodPlanCount AS int)) AS long) AS totalProductionAmount, " +
            "CAST(SUM(CAST(pp.prodPlanCount AS int)) / (SELECT SUM(CAST(pp2.prodPlanCount AS int)) FROM ProductPlanEntity pp2 WHERE pp2.productEntity.prodId = pp.productEntity.prodId) * 100 AS long) AS productionPercentage) " +
            "FROM ProductPlanEntity pp " +
            "JOIN pp.productEntity p " +
            "JOIN pp.allowApprovalEntity aa " +
            "WHERE aa.al_app_whether = true " +
            "GROUP BY p.prodName, p.prodPrice " +
            "ORDER BY p.prodName ASC")
    List<ProductProductionDto> findProductProduction();
    // 특이사항: nativeQuery 사용[판매실적 쿼리와 다른 접근]
}
