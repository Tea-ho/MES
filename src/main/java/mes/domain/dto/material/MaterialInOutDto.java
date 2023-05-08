package mes.domain.dto.material;

import lombok.*;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialInOutEntity;
import mes.domain.entity.member.AllowApprovalEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialInOutDto {

    private int mat_in_outid;// -- 원자재 입출고 PK
    private int mat_in_type;// -- + -
    private int mat_st_stock;// -- 남은 재고
    private AllowApprovalEntity allowApprovalEntity;
    private MaterialEntity materialEntity;// -- 마스터 원자재 테이블 fk
    private int MatID; // 검색용

    public MaterialInOutDto(int mat_in_outid, int mat_in_type, int mat_st_stock, AllowApprovalEntity allowApprovalEntity, MaterialEntity materialEntity) {
        this.mat_in_outid = mat_in_outid;
        this.mat_in_type = mat_in_type;
        this.mat_st_stock = mat_st_stock;
        this.allowApprovalEntity = allowApprovalEntity;
        this.materialEntity = materialEntity;
    }

    public MaterialInOutEntity toInEntity(){
        return MaterialInOutEntity.builder()
                .mat_in_outid(this.mat_in_outid)
                .mat_st_stock(this.mat_st_stock)
                .mat_in_type(this.mat_in_type)
                .materialEntity(this.materialEntity)
                .allowApprovalEntity(this.allowApprovalEntity)
                .build();






    }
}
