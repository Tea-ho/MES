package mes.domain.entity.sales;

import lombok.*;
import mes.domain.dto.sales.SalesDto;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.product.ProductEntity;

import javax.persistence.*;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_id;// -- 주문 D


    @Column private String orderDate;   // -- 주문 일자
    @Column private int orderCount;     // -- 주문 수량
    @Column private int order_status;    // -- 주문 상태
    @Column private int salesPrice;     // 판매가

    @ManyToOne
    @JoinColumn(name = "alAppNo")
    @ToString.Exclude
    private AllowApprovalEntity allowApprovalEntity;    //-- 결제 승인 여부 테이블 fk

    @ManyToOne
    @JoinColumn(name = "cno")
    @ToString.Exclude
    private CompanyEntity companyEntity;                // 고객처

    @ManyToOne
    @JoinColumn(name = "prodId")
    @ToString.Exclude
    private ProductEntity productEntity;                // -- 주문 제품

    @ManyToOne
    @JoinColumn(name = "mno")
    @ToString.Exclude
    private MemberEntity memberEntity;                  // -- 판매자(판매원)

    public SalesDto toDto() { // 출력용
        return SalesDto.builder()
                .order_id(this.order_id)
                .order_status(this.order_status)
                .orderCount(this.orderCount)
                .orderDate(this.orderDate)
                .salesPrice(this.salesPrice)
                .companyDto(this.companyEntity.toDto() )
                .prodId(this.productEntity.getProdId())
                .prodName(this.productEntity.getProdName())
                .build();
    }

}
