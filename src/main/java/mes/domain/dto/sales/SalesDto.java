package mes.domain.dto.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductPlanEntity;
import mes.domain.entity.product.ProductProcessEntity;
import mes.domain.entity.sales.SalesEntity;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesDto {

    private int orderId;// -- 주문 ID

    private String orderDate;   // 주문 일자
    private int orderCount;     // 주문 수량
    private int orderStatus;    // 주문 상태
    private int salesPrice;     // 판매가
    private AllowApprovalEntity allowApprovalEntity; // 결제 승인여부


    private CompanyEntity companyEntity; // 판매회사
    private int cno;

    private ProductEntity productEntity; // 주문 제품 ( 이름 )
    private String prodName;    // 완재품 이름
    private int prodId;         // 완제품 id
    private ProductProcessEntity productProcessEntity; // 주문 제품 ( 상태, 개수 )
    private int prodProcStatus; // 완재품 상태
    private int prodStock;      // 완재품 개수

    private MemberEntity memberEntity;   // 판매등록자(판매원)
    private int mname;

    private ProductPlanEntity productPlanEntity;

    public SalesEntity toEntity(){ // 저장용
        return SalesEntity.builder()
                .orderDate(this.orderDate)
                .orderCount(this.orderCount)
                .orderStatus(this.orderStatus)
                .salesPrice(this.salesPrice)
                .companyEntity(this.companyEntity)
                .productEntity(this.productEntity)
                .memberEntity(this.memberEntity)
                .build();
    }

}
