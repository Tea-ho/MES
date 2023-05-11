package mes.domain.Repository.product;

import mes.domain.entity.product.ProductProcessEntity;
import mes.domain.entity.sales.SalesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductProcessRepository extends JpaRepository<ProductProcessEntity, Integer> {

    @Query(value = "select * from product_process where if(:keyword = '', TRUE, prod_stock LIKE %:keyword%)" , nativeQuery = true)
    Page<ProductProcessEntity> findByPage(String keyword , Pageable pageable);

}
