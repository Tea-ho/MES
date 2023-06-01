package mes.service.Material;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.material.MaterialPageDto;
import mes.domain.dto.member.CompanyDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.CompanyRepository;
import mes.domain.entity.product.MaterialProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


    // 자재 등록1
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
    public MaterialPageDto materialList(MaterialPageDto dto){
        List<MaterialDto> list = new ArrayList<>();


        if(dto.getMatID() == 0){
            Pageable pageable = PageRequest.of(dto.getPage()-1 , 5 , Sort.by(Sort.Direction.DESC , "matID"));
            System.out.println("Servicedto : " + dto);
            Page<MaterialEntity> entityPage = materialEntityRepository.findByPage(dto.getKeyword() , pageable);
            entityPage.forEach((e)->{
                list.add(e.toDto());
            });

           dto.setMaterialList(list);
           dto.setTotalPage(entityPage.getTotalPages());
           dto.setTotalCount(entityPage.getTotalElements());
        }
        else if(dto.getMatID() > 0){
            MaterialEntity entity = materialEntityRepository.findById(dto.getMatID()).get();
            list.add(entity.toDto());

            dto.setMaterialList(list);
        }
        System.out.println("Servicedto : " + dto);



        return dto;
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
