package mes.domain.entity.product;

import lombok.*;

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
    @Column private int ProdStock;// -- 완제품 재고


    @ManyToOne
    @JoinColumn(name = "prodPlanNo")
    @ToString.Exclude
    private ProductPlanEntity productPlanEntity;// -- 마스터 제품 테이블 fk


}
