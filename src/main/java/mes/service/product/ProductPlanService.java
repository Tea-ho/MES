package mes.service.product;

import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.MaterialProductRepository;
import mes.domain.Repository.product.ProductPlanRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.material.MaterialDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.product.MaterialProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductPlanService {
    @Autowired
    ProductPlanRepository productPlanRepository;

    @Autowired
    MaterialProductRepository materialProductRepository;

    @Autowired
    MaterialEntityRepository materialEntityRepository;

    //제품별 자재에 비율 담아서 보내기
    public List<MaterialDto> getExistMaterialList(int prodId){
        List<MaterialProductEntity> materialProductEntities = materialProductRepository.findByMaterial(prodId);

        List<MaterialDto> materialDtoList = new ArrayList<>();

        for(int i = 0; i < materialProductEntities.size(); i++){
            MaterialDto materialDto = new MaterialDto();

            Optional<MaterialEntity> materialEntity = materialEntityRepository.findById(materialProductEntities.get(i).getMaterialEntity().getMatID());

            if(materialEntity.isPresent()){
                materialDto = materialEntity.get().toDto();
                materialDto.setRatio(materialProductEntities.get(i).getReferencesValue());
                materialDtoList.add(materialDto);
                System.out.println(materialDto);
            }
        }
        System.out.println(materialDtoList);
        return materialDtoList;
    }
}
