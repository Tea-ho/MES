package mes.domain.Repository.product;

import mes.domain.entity.product.ProductProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductProcessRepository extends JpaRepository<ProductProcessEntity, Integer> {

/*  // 재고 값 찾기 위한 @Query문 예상 ---> prod_proc_no 를 모른다는 sql 오류 발생. ->
        등록할때 prod_id도 알아야하고 , prod_proc_no도 알아야하나? 방법은?

    @Query(value = "select pprocess.prod_stock from product p inner join product_plan plan on p.prod_id=plan.prod_id inner join product_process pprocess on plan.prod_plan_no=pprocess.prod_plan_no where pprocess.prod_plan_no=plan.prod_plan_no and plan.prod_id = :prod_Id", nativeQuery = true)
    ProductProcessEntity findStockByProductId(@Param("prod_Id") int prod_Id);
*/


}
