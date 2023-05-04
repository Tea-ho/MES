package mes.service.Material;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.material.MaterialInOutDto;
import mes.domain.dto.member.AllowApprovalDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.material.MaterialInOutEntity;
import mes.domain.entity.material.MaterialInOutEntityRepository;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.AllowApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sun.javafx.util.Utils.sum;

@Service
@Slf4j
public class MaterialInoutService {

    @Autowired
    private MaterialInOutEntityRepository materialInOutEntityRepository;

    @Autowired
    private MaterialEntityRepository materialEntityRepository;

    @Autowired
    private AllowApprovalRepository allowApprovalRepository;



    // 입고내역
    @Transactional
    public boolean materialIn(MaterialInOutDto dto){

        List<MaterialInOutEntity> inOutEntityList = materialInOutEntityRepository.findAll();
        int stock=0;
        for( int i = 0; i < inOutEntityList.size(); i++){
            if(i == inOutEntityList.size()-1){
                stock = inOutEntityList.get(i).getMat_st_stock();

            }
        }

        MaterialInOutEntity entity = dto.toInEntity();


        // 선택된 자재
        Optional<MaterialEntity> optionalMaterial = materialEntityRepository.findById(dto.getMatID());
        MaterialEntity materialEntity = optionalMaterial.get();
        // 승인정보
        AllowApprovalEntity approvalEntity = allowApprovalRepository.save(new AllowApprovalDto().toInEntity());

        // 자재와 데이터 넣기
        entity.setMaterialEntity(materialEntity);
        entity.setAllowApprovalEntity(approvalEntity);
        entity.setMat_st_stock(stock+entity.getMat_in_type());

        // 세이브
        MaterialInOutEntity result = materialInOutEntityRepository.save(entity);

        if( result.getMat_in_outid() >= 1 ){ return true; }  // 2. 만약에 생성된 엔티티의 pk가 1보다 크면 save 성공
        return false;

    }

    // 내역 리스트 출력
    public List<MaterialInOutDto> MaterialInOutList(int matID){

       List<MaterialInOutDto> list = new ArrayList<>();
       // 스톡 저장 추후 결제후 스톡으로 변경
       List<MaterialInOutEntity> entityOptional = materialInOutEntityRepository.findByMatid(matID);



       // 리스트 출력
        entityOptional.forEach((e)->{
            list.add(e.toDto());
             }
        );



        return list;
    }

}
