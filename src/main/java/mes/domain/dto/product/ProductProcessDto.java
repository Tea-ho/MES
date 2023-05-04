package mes.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.entity.product.ProductPlanEntity;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductProcessDto {

    private int prodProcNo;// -- PK

    private String prodProcDate;// -- 공정 일자
    private int prodProcStatus;// -- 공정 상태
    private int ProdStock;// -- 완제품 재고 (+, - 아니고 = 으로)

    private ProductPlanEntity productPlanEntity;// -- 마스터 제품 테이블 fk
}
