package mes.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.product.MaterialProductEntity;
import mes.domain.entity.product.ProductEntity;


import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialProductDto {
    private int mpno;


    private List<MaterialEntity> materialEntityList = new ArrayList<>();


    private ProductEntity productEntity;

    //등록용 생성자
    public MaterialProductDto(ProductDto productDto, List<MaterialEntity> materialEntityList) {
    }

    //저장용
    public MaterialProductEntity toEntity(){
        return MaterialProductEntity.builder()
                .productEntity(this.productEntity)
                .build();
    }
}
