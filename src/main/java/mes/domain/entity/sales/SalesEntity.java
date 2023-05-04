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
    private int OrderId;// -- 주문 D


    @Column private String OrderDate;   // -- 주문 일자
    @Column private int OrderCount;     // -- 주문 수량
    @Column private int OrderStatus;    // -- 주문 상태
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
                .OrderId(this.OrderId)
                .OrderCount(this.OrderCount)
                .OrderDate(this.OrderDate)
                .salesPrice(this.salesPrice)
                .allowApprovalEntity(this.getAllowApprovalEntity())
                .companyEntity(this.getCompanyEntity())
                .memberEntity(this.getMemberEntity())
                .productEntity(this.getProductEntity())
                .build();
    }

}
