package mes.domain.entity.sales;

import mes.domain.dto.sales.SalesByCompanyDto;
import mes.domain.dto.sales.SalesByMemberDto;
import mes.domain.dto.sales.SalesByProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository< SalesEntity , Integer > {
// 생산실적 및 판매실적 Query 구성 인터페이스 (추후 합치기 작업 필요)

    // page , key , keyword [ 판매 회사명, 판매상태 검색 ] --> 추후 : 판매개수 , 판매액 등등?
/*    @Query(value = "select * " +
            "from sales s inner join company c " +
            "on s.cno = c.cno and if(:key = '', true, " +
            "if(:key = 'orderStatus' , s.order_status like %:keyword% , c.cname like %:keyword%))",
            nativeQuery = true )
    Page<SalesEntity> findBySearch(Pageable pageable , String key , String keyword);*/

    // sales order_status 값 찾기 위함
    @Query(value = "select * from sales where if(:keyword = '', TRUE, order_status LIKE %:keyword%)" , nativeQuery = true)
    Page<SalesEntity> findByPage(String keyword , Pageable pageable);

    // 판매처별 판매실적 쿼리
    @Query(value = "SELECT new com.example.dto.SalesByCompanyDto(s.salesPrice, c.toDto()) " +
            "FROM SalesEntity s " +
            "JOIN s.companyEntity c " +
            "WHERE c.companyId = :companyId", nativeQuery = true)
    List<SalesByCompanyDto> findSalesByCompany(@Param("companyId") int companyId);

    // 판매원별 판매실적 쿼리
    @Query(value = "SELECT new com.example.dto.SalesByMemberDto(s.salesPrice, m.toDto()) " +
            "FROM SalesEntity s " +
            "JOIN s.memberEntity m " +
            "WHERE m.memberId = :memberId", nativeQuery = true)
    List<SalesByMemberDto> findSalesByMember(@Param("memberId") int memberId);

    // 제품별 판매실적 쿼리
    @Query(value = "SELECT new com.example.dto.SalesByProductDto(s.salesPrice, p.prodName) " +
            "FROM SalesEntity s " +
            "JOIN s.productEntity p " +
            "WHERE p.prodId = :productId", nativeQuery = true)
    List<SalesByProductDto> findSalesByProduct(@Param("productId") int productId);
}
