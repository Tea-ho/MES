package mes.domain.entity.product;

import lombok.*;
import mes.domain.dto.product.ProductProcessDto;

import javax.persistence.*;

@Entity
@Table(name = "productProcess")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prodProcNo;// -- PK
    @Column private String prodProcDate;// -- 공정 일자
    @Column private int prodProcStatus;// -- 공정 상태
    @Column private int prodStock;// -- 완제품 재고 (+, - 아니고 = 으로)


    @ManyToOne
    @JoinColumn(name = "prodId")
    @ToString.Exclude
    public ProductEntity productEntity;// -- 마스터 제품 테이블 fk

    public ProductProcessDto toDto() { // 판매 쪽 제품공정 출력용
        return  ProductProcessDto.builder()
                .prodProcNo(this.prodProcNo)
                .prodProcDate(this.prodProcDate)
                .prodStock(this.prodStock)
                .prodId(this.productEntity.getProdId())
                .prodName(this.productEntity.getProdName())
                .build();
    }


}
