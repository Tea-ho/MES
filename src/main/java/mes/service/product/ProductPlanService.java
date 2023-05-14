package mes.service.product;

import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.MaterialProductRepository;
import mes.domain.Repository.product.ProductPlanRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.member.AllowApprovalDto;
import mes.domain.dto.product.ProductDto;
import mes.domain.dto.product.ProductPlanDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.material.MaterialInOutEntity;
import mes.domain.entity.material.MaterialInOutEntityRepository;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.AllowApprovalRepository;
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

    @Autowired
    MaterialInOutEntityRepository materialInOutEntityRepository;

    @Autowired
    AllowApprovalRepository allowApprovalRepository;

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

    // 제품 생산 지시 => 자재 재고 감소시켜야하는데, 자재 하나라도 재고 부족하면, 생산 지시 막아야 함
    public List<String> postProduct(ProductPlanDto productPlanDto){
        boolean OKsign = true; //제품 생산 지시 여부 => 유효성 검사(자재 재고) 통과하면 그대로 남는다.

        ArrayList<String> resultString = new ArrayList<>(); //결과 문자열

        System.out.println("post Product : " + productPlanDto.toString());

        //productEntity에 연결된 materialList 찾아서
        List<MaterialProductEntity> materialProductEntities = materialProductRepository.findByMaterial(productPlanDto.getProductDto().getProdId());
        ArrayList<MaterialInOutEntity> materialInOutEntities = new ArrayList<>(); //자재별 재고들 찾기

        //자재 재고 유효성 검사
        for(int i = 0; i < materialProductEntities.size(); i++){
            //하나씩 자재 목록의 정보를 받아와서 재고 리스트를 받는다.
            List<MaterialInOutEntity> materialInOutEntitiy = materialInOutEntityRepository.findByMid(materialProductEntities.get(i).getMaterialEntity().getMatID());

            int existMatStock = 0; //존재하는 자재 재고 누적해서 구하기

            //자재 재고 모두 더해서 가져오기
            for(int j = 0; j < materialInOutEntitiy.size(); j++) {
                existMatStock += materialInOutEntitiy.get(j).getMat_st_stock();
            }

            // 하나씩 엔티티로 변환
            MaterialInOutEntity materialInOut = materialInOutEntitiy.get(i);
            System.out.println("자재 재고 엔티티 "+ i +"번째 하나 변환 : " +materialInOut);

            //자재별 필요한 자재 재고
            int needMatStock = (materialProductEntities.get(i).getReferencesValue()) * Integer.parseInt(productPlanDto.getProdPlanCount());

            //재고 여부 확인
            if(existMatStock < needMatStock) { //자재 재고 부족하면
                OKsign = false;
                String strResult = materialInOut.getMaterialEntity().getMatID() + "번 자재의 재고가 " + (needMatStock - materialInOut.getMat_st_stock()) + "개 부족합니다.";
                resultString.add(strResult);
            }else{ //자재 재고 부족하지 않으면 아래서 재고 빼주기 위해 List에 추가해준다.
                MaterialInOutEntity materialInOutEntity = new MaterialInOutEntity();
                //필요한 정보 장착하기
                materialInOutEntity.setMaterialEntity(materialInOut.getMaterialEntity());
                materialInOutEntity.setMat_st_stock(-(needMatStock));
                materialInOutEntity.setMat_in_code(materialInOut.getMat_in_code());
                materialInOutEntity.setMat_in_type(materialInOut.getMat_in_type());

                // 승인정보
                AllowApprovalEntity approvalEntity = allowApprovalRepository.save(new AllowApprovalDto().toInEntity());

                materialInOutEntities.add(materialInOutEntity);
            }
        }

        if(OKsign){ //제품 생산 지시 넘기고, 자재 재고 감소 시킨다.
            //자재 재고 처리
            for(int i = 0; i < materialInOutEntities.size(); i++){

            }
            String strResult = "제품 생산 지시가 완료 되었습니다.";
        }


        return resultString;
    }
}
