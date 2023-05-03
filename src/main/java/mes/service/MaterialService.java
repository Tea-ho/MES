package mes.service;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.material.MaterialDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;

@Service
@Slf4j
public class MaterialService {

    @Autowired
    MaterialEntityRepository materialEntityRepository;

    @Transactional
    public boolean materialCreate(MaterialDto dto){

        MaterialEntity entity = materialEntityRepository.save(dto.toEntity());
        if( entity.getMatID() >= 1 ){ return true; }

        return false;
    }
}
