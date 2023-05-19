package mes.domain.Repository.product;

import mes.domain.dto.product.AutoProduceDto;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductProcessEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductProcessRepository extends JpaRepository<ProductProcessEntity, Integer> {

    @Query(value = "select * from product_process where if(:keyword = '', TRUE, prod_stock LIKE %:keyword%)" , nativeQuery = true)
    Page<ProductProcessEntity> findByPage(String keyword , Pageable pageable);

    //제품 삭제 확인용
    @Query(value = "select * from product_process where prod_id = :prodId ", nativeQuery = true)
    List<ProductProcessEntity> findByProductEntity(int prodId);

    // 제품 stock 확인용
    @Query(value = "select * from product_process where prod_id = :prodId" , nativeQuery = true)
    ProductProcessEntity findByProdId(int prodId);
    // 제품재고 누적 추가를 위해 생성 [23.05.18, th]
    ProductProcessEntity findByProductEntity(ProductEntity productEntity);

    // 제품별 조회: 제품명, 평균 판매량, 안전재고량(현재고*1.2)
    @Query(value = "SELECT p.prod_id AS prodID, AVG(s.order_count) AS orderCountAVG, pp.prod_stock * 1.2 AS prodSafeStock " +
            "FROM product_process pp " +
            "JOIN sales s ON pp.prod_id = s.prod_id " +
            "JOIN product p ON p.prod_id = pp.prod_id " +
            "GROUP BY pp.prod_id, pp.prod_stock", nativeQuery = true)
    List<AutoProduceDto> getAverageSalesAndCurrentStock();

    // 제품별 조회: 제품명, 평균 판매량, 평균 생산량
    @Query(value = "SELECT p.prod_id AS prodID, AVG(s.order_count) AS avgOrderCount, AVG(pp.prod_plan_count) AS avgProdPlanCount " +
            "FROM product p " +
            "JOIN sales s ON p.prod_id = s.productEntity.prodId " +
            "JOIN product_plan pp ON p.prod_id = pp.productEntity.prodId " +
            "GROUP BY p.prod_id", nativeQuery = true)
    List<AutoProduceDto> getProductSalesAndProduction();


    // 제품별 조회: 제품명, 제품 현재 재고, 제품 평균 판매량, 제품 평균 생산량
    @Query(value = "SELECT p.prod_id AS prodID, pp.prod_stock AS prodCurrentStock, AVG(s.order_count) AS avgOrderCount, AVG(pp.prod_plan_count) AS avgProdPlanCount " +
            "FROM product p " +
            "JOIN product_plan ppp ON p.prod)id = ppp.productEntity.prodId " +
            "JOIN product_process pp ON p.prod_id = pp.productEntity.prodId " +
            "JOIN sales s ON pp.productEntity.prodId = s.productEntity.prodId " +
            "GROUP BY pp.prod_id, pp.prod_stock", nativeQuery = true)
    List<AutoProduceDto> getCurrentStockAndAverageSales();

}
