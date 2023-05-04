package mes.domain.entity.material;

import lombok.*;
import mes.domain.BaseTime;
import mes.domain.dto.material.MaterialInOutDto;
import mes.domain.entity.member.AllowApprovalEntity;

import javax.persistence.*;

@Entity
@Table(name = "materialInOut")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialInOutEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mat_in_outid;// -- 원자재 입출고 PK

    @Column private int mat_in_type;// -- + -
    @Column private int mat_st_stock;// -- 남은 재고

    @ManyToOne // 다수가 하나에게 [fk ---> pk]
    @JoinColumn(name = "al_app_no") //pk 이름 정하기
    @ToString.Exclude
    private AllowApprovalEntity allowApprovalEntity;

    @ManyToOne // 다수가 하나에게 [fk ---> pk]
    @JoinColumn(name = "MatID") //pk 이름 정하기
    @ToString.Exclude
    private MaterialEntity materialEntity;// -- 마스터 원자재 테이블 fk

    public MaterialInOutDto toDto(){
        return MaterialInOutDto.builder()
                .mat_in_outid(this.mat_in_outid)
                .materialEntity(this.materialEntity)
                .allowApprovalEntity(this.allowApprovalEntity)
                .mat_in_type(this.mat_in_type)
                .mat_st_stock(this.mat_st_stock)
                .build();

    }

}
