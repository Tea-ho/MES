package mes.domain.entity.member;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "allowApproval")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowApprovalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int alAppNo; //-- PK
    @Column private boolean alAppWhether;//  -- 결재자 승인 여부
    @Column private String alAppDate; // 승인 일자
    @Column private String alAppRole; // 승인 처리인지 승인 요청인지


    @ManyToOne
    @JoinColumn(name = "mno")
    @ToString.Exclude
    private MemberEntity memberEntity; // 승인 요청 or 처리한 사람

    // ----------------------------n:n or n:1 관계 설정 필요
    
}
