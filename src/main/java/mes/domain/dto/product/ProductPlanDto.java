package mes.domain.dto.product;

import lombok.*;
import mes.domain.dto.member.AllowApprovalDto;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductPlanEntity;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPlanDto {

    private int prodPlanNo;// -- PK

    private String prodPlanCount;// -- 제품 생산 수량

    private String prodPlanDate;// -- 생산 예정 일자

    private ProductDto productDto;// -- 마스터 제품 테이블 fk

    private AllowApprovalDto allowApprovalDto;// -- 결제 승인 여부 테이블 fk

    public ProductPlanEntity toEntity(){
        return ProductPlanEntity.builder()
                .prodPlanCount(this.prodPlanCount)
                .prodPlanDate(this.prodPlanDate)
                .productEntity(this.productDto.toEntity())
                .allowApprovalEntity(this.allowApprovalDto.toInEntity())
                .build();

    }
}
