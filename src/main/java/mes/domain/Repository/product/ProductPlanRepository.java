package mes.domain.Repository.product;

import mes.domain.dto.product.ProductProductionByMonthDto;
import mes.domain.dto.product.ProductProductionByQuarterDto;
import mes.domain.dto.product.ProductProductionByYearDto;
import mes.domain.dto.product.ProductProductionDto;
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
    
    // 2. 제품별 판매실적 쿼리[출력내용: 제품명, 제품생산량, 월수, 조건: 생산 승인 완료, 그룹핑 조건: 제품명 + 월수]
    @Query(value ="SELECT p.productEntity.prodName, SUM(p.prodPlanCount), MONTH(p.prodPlanDate) " +
            "FROM ProductPlanEntity p " +
            "JOIN p.allowApprovalEntity a " +
            "WHERE a.al_app_whether = true " +
            "GROUP BY p.productEntity.prodName, MONTH(p.prodPlanDate)", nativeQuery = true)
    List<ProductProductionByMonthDto> findProductProductionByMonth();

    // 3. 제품별 판매실적 쿼리[출력내용: 제품명, 제품생산량, 분기수, 조건: 생산 승인 완료, 그룹핑 조건: 제품명 + 분기수]
    @Query(value ="SELECT p.productEntity.prodName, SUM(p.prodPlanCount), QUARTER(p.prodPlanDate) " +
            "FROM ProductPlanEntity p " +
            "JOIN p.allowApprovalEntity a " +
            "WHERE a.al_app_whether = true " +
            "GROUP BY p.productEntity.prodName, QUARTER(p.prodPlanDate)", nativeQuery = true)
    List<ProductProductionByQuarterDto> findProductProductionByQuarter();
    
    // 4. 제품별 판매실적 쿼리[출력내용: 제품명, 제품생산량, 연수, 조건: 생산 승인 완료, 그룹핑 조건: 제품명 + 연수]
    @Query(value ="SELECT p.productEntity.prodName, SUM(p.prodPlanCount), YEAR(p.prodPlanDate) " +
            "FROM ProductPlanEntity p " +
            "JOIN p.allowApprovalEntity a " +
            "WHERE a.al_app_whether = true " +
            "GROUP BY p.productEntity.prodName, YEAR(p.prodPlanDate)", nativeQuery = true)
    List<ProductProductionByYearDto> findProductProductionByYear();

}
