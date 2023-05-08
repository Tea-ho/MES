package mes.domain.entity.product;

import lombok.*;
import mes.domain.entity.member.AllowApprovalEntity;

import javax.persistence.*;

@Entity
@Table(name = "productPlan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prodPlanNo;// -- PK
    @Column private String prodPlanCount;// -- 제품 생산 수량
    @Column private String prodPlanDate;// -- 생산 예정 일자

    @ManyToOne
    @JoinColumn(name = "prod_id")
    @ToString.Exclude
    private ProductEntity productEntity;// -- 마스터 제품 테이블 fk

    @ManyToOne
    @JoinColumn(name = "al_app_no")
    @ToString.Exclude
    private AllowApprovalEntity allowApprovalEntity;// -- 결제 승인 여부 테이블 fk

}
