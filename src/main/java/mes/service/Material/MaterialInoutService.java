package mes.service.Material;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.material.InOutPageDto;
import mes.domain.dto.material.MaterialInOutDto;
import mes.domain.dto.member.AllowApprovalDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.material.MaterialInOutEntity;
import mes.domain.entity.material.MaterialInOutEntityRepository;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.AllowApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        List<MaterialInOutEntity> inOutEntityList = materialInOutEntityRepository.findByMid(dto.getMatID());
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
    public InOutPageDto MaterialInOutList(InOutPageDto dto){

       List<MaterialInOutDto> list = new ArrayList<>();
        Pageable pageable = PageRequest.of(dto.getPage()-1 , 5 , Sort.by(Sort.Direction.DESC , "mat_in_outid"));

        Page<MaterialInOutEntity> entityPage = materialInOutEntityRepository.findByMatid(dto.getMatID() , pageable);
        entityPage.forEach((e)->{
            list.add(e.toDto());
        });

        dto.setMaterialInOutDtoList(list);
        dto.setTotalPage(entityPage.getTotalPages());
        dto.setTotalCount(entityPage.getTotalElements());

        return dto;
    }

}
