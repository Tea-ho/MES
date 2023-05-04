package mes.service.Material;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.member.CompanyDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class MaterialService {

    @Autowired
    MaterialEntityRepository materialEntityRepository;

    @Autowired
    CompanyRepository companyRepository;


    // 자재 등록
    @Transactional
    public boolean materialCreate(MaterialDto dto){


        CompanyEntity companyEntity = companyRepository.findById(dto.getCno()).get();

        MaterialEntity entity = materialEntityRepository.save(dto.toEntity());
        entity.setCompanyEntity(companyEntity);

        log.info("Material entity"+ entity);
        if( entity.getMatID() >= 1 ){ return true; }

        return false;
    }

    // 자재 리스트 출력
    @Transactional
    public List<MaterialDto> materialList(int matID){
        List<MaterialDto> list = new ArrayList<>();
        if(matID == 0){
            List<MaterialEntity> entityOptional = materialEntityRepository.findAll();
            entityOptional.forEach((e)->{
                list.add(e.toDto());
            });
        }
        else if(matID > 0){
            MaterialEntity entity = materialEntityRepository.findById(matID).get();
            list.add(entity.toDto());
        }




        return list;
    }


    // 회사불러오기
    @Transactional
    public List<CompanyDto> getCompany(){
        List<CompanyDto> dto = new ArrayList<>();
        List<CompanyEntity> entityOptional = companyRepository.findAll();

        entityOptional.forEach((e)->{
           dto.add(e.toDto());
        });
        return dto;
    }
}
