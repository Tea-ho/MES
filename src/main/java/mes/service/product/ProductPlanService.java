package mes.service.product;

import lombok.extern.slf4j.Slf4j;
import mes.controller.member.MemberController;
import mes.domain.Repository.product.MaterialProductRepository;
import mes.domain.Repository.product.ProductPlanRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.material.MaterialInOutDto;
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
import mes.domain.entity.product.ProductPlanEntity;
import mes.service.Material.MaterialInoutService;
import mes.service.member.MemberSerivce;
import mes.webSocket.ChattingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    MaterialInoutService materialInoutService;

    @Autowired
    private ChattingHandler chattingHandler; //소켓

    //생산 지시 목록 가져오기
    public List<ProductPlanDto> getPlanProductList(){
        List<ProductPlanEntity> productPlanEntitiesList = productPlanRepository.findAll();

        List<ProductPlanDto> productPlanDtoList = new ArrayList<>();

        for(int i = 0; i < productPlanEntitiesList.size(); i++){
            productPlanDtoList.add(productPlanEntitiesList.get(i).toDto());
        }

        return productPlanDtoList;
    }

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

    @Transactional
    // 제품 생산 지시 => 자재 재고 감소시켜야하는데, 자재 하나라도 재고 부족하면, 생산 지시 막아야 함
    public List<String> postProduct(ProductPlanDto productPlanDto){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //date format

        boolean okSign = true; //제품 생산 지시 여부 => 유효성 검사(자재 재고) 통과하면 그대로 남는다.

        ArrayList<String> returnResultStr = new ArrayList<>();  //js에 반환할 내용

        //productEntity에 연결된 materialList 찾아서 (+기준량)
        List<MaterialProductEntity> materialProductEntities = materialProductRepository.findByMaterial(productPlanDto.getProductDto().getProdId());

        // mpno 개수 = materials 개수
        for(int i = 0; i < materialProductEntities.size(); i++){

            //자재 목록
            Optional<MaterialEntity> materialEntity = materialEntityRepository.findById(materialProductEntities.get(i).getMaterialEntity().getMatID());

            List<MaterialInOutEntity> materialInOutEntityList = new ArrayList<>(); //반복문 돌때마다 초기화를 해야 자재마다 재고를 구할 수 있다.

            if(materialEntity.isPresent()){
                int existMatStock = 0; //존재하는 자재 재고 누적해서 구하기
                int needMatStock = 0; // 필요한 자재 재고

                materialInOutEntityList = materialInOutEntityRepository.findByMaterial(materialEntity.get().getMatID()); //자재 하나당 가지고 있는 inout

                for(int j = 0; j < materialInOutEntityList.size(); j++){
                    existMatStock += materialInOutEntityList.get(j).getMat_in_type(); // 자재 재고 누적 더하기(존재하는 총 재고)
                }
                System.out.println(materialEntity.get().getMat_name() + "자재 총 재고 : " + existMatStock);

                //제품을 만들기 위해 각각의 자재 필요한 재고
                needMatStock = Integer.parseInt(productPlanDto.getProdPlanCount()) * (materialProductEntities.get(i).getReferencesValue());
                System.out.println(materialEntity.get().getMat_name() + "자재 필요한 재고 : " + needMatStock);

                if(existMatStock < needMatStock){ //재고가 부족하면.
                    okSign = false; //한개라도 부족하면 제품을 만들 수 없음.
                    String str = "[알림]"+materialEntity.get().getMatID()+"-"+materialEntity.get().getMat_name() + "자재의 재고가 " + (needMatStock - existMatStock) +materialEntity.get().getMat_unit()+" 부족합니다.";
                    returnResultStr.add(str); //재고 부족 알림을 String 배열에 담는다.
                }
            }
        }

        boolean mat_okOut = true; //자재 재고가 모두 감소되었는지 확인용
        ArrayList<AllowApprovalEntity> allowApprovalEntityArrayList = new ArrayList<>();

        if(okSign){ //자재의 재고가 모두 충분한게 확인이 되었음

            //자재 재고를 이제 빼준다.
            //material_product row의 개수 = material row 개수
            for(int i = 0; i < materialProductEntities.size(); i++){
                Optional<MaterialEntity> materialEntity = materialEntityRepository.findById(materialProductEntities.get(i).getMaterialEntity().getMatID());

                List<MaterialInOutEntity> materialInOutEntityList = new ArrayList<>(); //반복문 돌때마다 초기화를 해야 자재마다 재고를 구할 수 있다.
                if(materialEntity.isPresent()){
                    MaterialInOutDto dto = new MaterialInOutDto();

                    AllowApprovalDto allowApprovalDto = new AllowApprovalDto();

                    allowApprovalDto.setAl_app_whether(true); //제품은 승인X

                    dto.setMaterialDto(materialEntity.get().toDto());
                    dto.setAllowApprovalDto(allowApprovalDto);

                    int needMatStock = Integer.parseInt(productPlanDto.getProdPlanCount()) * (materialProductEntities.get(i).getReferencesValue());
                    int existMatStock = 0; //존재하는 자재 재고 누적해서 구하기

                    materialInOutEntityList = materialInOutEntityRepository.findByMaterial(materialEntity.get().getMatID()); //자재 하나당 가지고 있는 inout

                    for(int j = 0; j < materialInOutEntityList.size(); j++){
                        existMatStock += materialInOutEntityList.get(j).getMat_in_type(); // 자재 재고 누적 더하기(존재하는 총 재고)
                    }

                    System.out.println("재고 현황 ? " + existMatStock);

                    dto.setMat_st_stock(existMatStock - needMatStock); //재고처리
                    dto.setMat_in_type(-needMatStock); // 차감되는 재고
                    dto.setMat_in_code(1);
                    dto.setMemberdto(productPlanDto.getMemberDto());

                    AllowApprovalEntity allowApprovalEntity = materialInoutService.materialOut(dto);

                    if(allowApprovalEntity!= null){
                        allowApprovalEntityArrayList.add(allowApprovalEntity);
                    }else{
                        mat_okOut = false;
                    }
                    log.info("materialOut service에 전달 : " + dto + "출고 결과 : " + mat_okOut);

                }
            }
        }

        //재고가 무사히 다 적용이 되었다면
        if(mat_okOut && okSign){
            //제품 생산 지시
            AllowApprovalEntity inAllowapproval = AllowApprovalEntity.builder()
                    .al_app_date(simpleDateFormat.format(new Date()))
                    .al_app_whether(false)
                    .build();

            System.out.println("확인 " + inAllowapproval);

            AllowApprovalEntity allowApprovalEntity = allowApprovalRepository.save(inAllowapproval);
            System.out.println("저장되었다면 확인 : " + allowApprovalEntity);

            productPlanDto.setAllowApprovalDto(allowApprovalEntity.toPlanDto());
            ProductPlanEntity productPlanEntity = productPlanDto.toEntity();
            productPlanEntity.setProdPlanDate(simpleDateFormat.format(new Date()));

            String date = allowApprovalEntity.getAl_app_date() +"/"+allowApprovalEntity.getAl_app_no();

            System.out.println("productPlan DB에 들어가기 전 : " + productPlanEntity);

            ProductPlanEntity entity = productPlanRepository.save(productPlanEntity);

            System.out.println("productPlan 엔티티 DB에 저장 완료 " + entity);

            log.info("생산 지시에 들어간 productPlan" + entity.toString());

            if(entity.getProdPlanNo() > 0){ //생산 지시가 들어갔으면
                for(int i = 0; i < allowApprovalEntityArrayList.size(); i++){
                    System.out.println(allowApprovalEntityArrayList.get(i));
                    allowApprovalEntityArrayList.get(i).setAl_app_date(date);
                }
                returnResultStr.add("[성공]생산지시가 완료되었습니다.");
            }
        }

        try{
            chattingHandler.handleMessage(null , new TextMessage("20"));

        }catch (Exception e){
            System.out.println(e);
        }

        return returnResultStr;
    }

    //생산 지시 취소
    public String deleteProduct(int prodPlanNo){
        //prodPlanNo로 해당 ProductPlanEntity찾기 => ProductPlanEntity에 승인 정보를 가지고 있음
        Optional<ProductPlanEntity> productPlanEntity = productPlanRepository.findById(prodPlanNo);
        System.out.println("생산 지시 취소 : " + productPlanEntity);

        boolean materialOk = materialInoutService.materialCancel(productPlanEntity.get().getAllowApprovalEntity().getAl_app_no());

        if(productPlanEntity.isPresent() && materialOk) {
           Optional<AllowApprovalEntity> allowApprovalEntity = allowApprovalRepository.findById(productPlanEntity.get().getAllowApprovalEntity().getAl_app_no());
            System.out.println("생산 지시 관련 allow : " + allowApprovalEntity);
           if(allowApprovalEntity.isPresent()){
               productPlanRepository.delete(productPlanEntity.get());
               allowApprovalRepository.delete(allowApprovalEntity.get());
               return "[성공]"+ prodPlanNo+ "번 생산 지시가 삭제 완료되었습니다.";
           }
        }
        return "[에러] 알 수 없는 에러가 발생하여 해당 " + prodPlanNo +"번째 생산 지시를 삭제할 수 없습니다.";
    }
}
