package mes.domain.entity.product;

import lombok.*;
import mes.domain.entity.member.CompanyEntity;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  prodId;// -- PK
    @Column
    private String prodName;// -- 제품명
    @Column
    private byte prodCode;// -- 제품 구분 문자코드 (식별용)
    @Column private String prodDate;// -- 생산일자
    @Column private int prodPrice;// -- 제품 가격
    
    @ManyToOne
    @JoinColumn(name = "cno")
    @ToString.Exclude
    private CompanyEntity companyEntity; // 회사명

}
