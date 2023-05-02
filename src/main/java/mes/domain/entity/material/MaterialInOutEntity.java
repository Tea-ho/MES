package mes.domain.entity.material;

import lombok.*;
import mes.domain.BaseTime;
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
    private int MatInOutID;// -- 원자재 입출고 PK

    @Column private int MatInType;// -- + -
    @Column private int MatStStock;// -- 남은 재고

    @ManyToOne // 다수가 하나에게 [fk ---> pk]
    @JoinColumn(name = "alAppNo") //pk 이름 정하기
    @ToString.Exclude
    private AllowApprovalEntity allowApprovalEntity;

    @ManyToOne // 다수가 하나에게 [fk ---> pk]
    @JoinColumn(name = "MatID") //pk 이름 정하기
    @ToString.Exclude
    private MaterialEntity materialEntity;// -- 마스터 원자재 테이블 fk

}
