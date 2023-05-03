package mes.domain.dto.material;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mes.domain.BaseTime;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.member.CompanyEntity;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialDto{

    private int MatID;// -- 원자재 ID (PK)

    private String MatCode;// -- 자재 구분 문자코드[ 입출고로 넘어갈 때 B로 바꾸기] (식별용)
    private String MatName;// -- 원자재명
    private String MatUnit;// -- 자재 단위
    private String MatStExp;// -- 유통기한
    private int MatPrice;// -- 단가
    private CompanyEntity companyEntity;// -- 제조사
    private LocalDateTime mdate; // 등록날짜

    public MaterialEntity toEntity() { // 저장용 추후 추가할것있음
        return   MaterialEntity.builder()
                .MatName(this.MatName)
                .MatUnit(this.MatUnit)
                .MatStExp(this.MatStExp)
                .MatPrice(this.MatPrice)
                .build();
    }

}
