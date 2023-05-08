package mes.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.BaseTime;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.product.ProductEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    private List<Integer> materialList; //자재 리스트(PK)

    private CompanyEntity companyEntity; // 회사명

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ProductEntity toEntity(){
        return ProductEntity.builder()
                .prodCode(this.prodCode)
                .prodDate(simpleDateFormat.format(new Date()))
                .prodPrice(this.prodPrice)
                .prodName(this.prodName)
                .companyEntity(this.companyEntity)
                .build();

    }
}
