package mes.domain.entity.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends JpaRepository< SalesEntity , Integer > {
// 생산실적 및 판매실적 Query 구성 인터페이스 (추후 합치기 작업 필요)


}
