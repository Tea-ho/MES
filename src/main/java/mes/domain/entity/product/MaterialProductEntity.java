package mes.domain.entity.product;

import lombok.*;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.member.MemberEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materialProduct")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mpno;

    @ManyToOne
    @JoinColumn(name = "prodId")
    @ToString.Exclude
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "MatID")
    @ToString.Exclude
    private MaterialEntity materialEntity;

}