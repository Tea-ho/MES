package mes.domain.dto.product;

import lombok.*;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.product.ProductEntity;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPlanDto {

    private int prodPlanNo;// -- PK

    private String prodPlanCount;// -- 제품 생산 수량

    private String prodPlanDate;// -- 생산 예정 일자

    private ProductEntity productEntity;// -- 마스터 제품 테이블 fk

    private AllowApprovalEntity allowApprovalEntity;// -- 결제 승인 여부 테이블 fk

}
