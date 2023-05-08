package mes.domain.Repository.product;

import mes.domain.entity.product.MaterialProductEntity;
import mes.domain.entity.product.ProductPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialProductRepository extends JpaRepository<MaterialProductEntity, Integer> {
}
