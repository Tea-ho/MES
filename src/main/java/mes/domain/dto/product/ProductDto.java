package mes.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.entity.member.CompanyEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private int  prodId;// -- PK

    private String prodName;// 제품명

    private String prodCode;// -- 제품 구분 문자코드 (식별용)
    private String prodDate;// -- 생산일자
    private int prodPrice;// -- 제품 가격

    private CompanyEntity companyEntity; // 회사명
}
