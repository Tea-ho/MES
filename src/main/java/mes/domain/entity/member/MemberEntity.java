package mes.domain.entity.member;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno; // pk

    @Column
    private String position; //회사 직급/포지션

    @ManyToOne
    @JoinColumn(name = "cNo")
    @ToString.Exclude
    private CompanyEntity companyEntity; // 회사명

}
