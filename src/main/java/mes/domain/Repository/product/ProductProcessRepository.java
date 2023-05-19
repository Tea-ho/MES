package mes.domain.Repository.product;

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

    ProductProcessEntity findByProductEntity(ProductEntity productEntity);

    //제품 삭제 확인용
    @Query(value = "select * from product_process where prod_id = :prodId ", nativeQuery = true)
    List<ProductProcessEntity> findByProductEntity(int prodId);

    // 제품 stock 확인용
    @Query(value = "select * from product_process where prod_id = :prodId" , nativeQuery = true)
    ProductProcessEntity findByProdId(int prodId);

}
