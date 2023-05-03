package mes.domain.entity.material;

import lombok.*;
import mes.domain.BaseTime;
import mes.domain.dto.material.MaterialDto;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.product.MaterialProductEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "material")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialEntity extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int MatID;// -- 원자재 ID (PK)
    @Column private String MatCode;// -- 자재 구분 문자코드[ 입출고로 넘어갈 때 B로 바꾸기] (식별용)
    @Column private String MatName;// -- 원자재명
    @Column private String MatUnit;// -- 자재 단위
    @Column private String MatStExp;// -- 유통기한
    @Column private int MatPrice;// -- 단가

    @ManyToOne // 다수가 하나에게 [fk ---> pk]
    @JoinColumn(name = "cno") //pk 이름 정하기
    @ToString.Exclude
    private CompanyEntity companyEntity;// -- 제조사

    @ManyToOne
    @JoinColumn(name = "mpno")
    private MaterialProductEntity materialProductEntity;


    public MaterialDto toDto() { // 반환용
        return MaterialDto.builder()
                .MatID(this.MatID)
                .MatStExp(this.MatStExp)
                .MatCode(this.MatCode)
                .MatUnit(this.MatUnit)
                .MatPrice(this.MatPrice)
                .companyEntity(this.getCompanyEntity())
                .mdate(this.cdate)
                .build();
    }

    

}
