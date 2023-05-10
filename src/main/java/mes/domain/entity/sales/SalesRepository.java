package mes.domain.entity.sales;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends JpaRepository< SalesEntity , Integer > {
// 생산실적 및 판매실적 Query 구성 인터페이스 (추후 합치기 작업 필요)

    // page , key , keyword [ 판매 회사명, 판매상태 검색 ] --> 추후 : 판매개수 , 판매액 등등?
    @Query(value = "select * " +
            "from sales s inner join company c" +
            "on s.cno = c.cno and if(:key = '', true," +
            "if(:key = 'orderStatus' , s.order_status like %:keyword% , c.cname like %keyword%))",
            nativeQuery = true )
    Page<SalesEntity> findBySearch(Pageable pageable , String key , String keyword);
}
