package mes.service.member;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.sales.SalesDto;
import mes.domain.entity.member.MemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mes.domain.Repository.product.ProductPlanRepository;
import mes.domain.dto.material.MaterialInOutDto;
import mes.domain.dto.product.ProductPlanDto;
import mes.domain.entity.material.MaterialInOutEntity;
import mes.domain.entity.material.MaterialInOutEntityRepository;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.AllowApprovalRepository;
import mes.domain.entity.member.PermissionDeniedException;
import mes.domain.entity.product.ProductPlanEntity;
import mes.domain.entity.sales.SalesEntity;
import mes.domain.entity.sales.SalesRepository;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
@Service @Slf4j
public class AllowApprovalService {

    @Autowired ProductPlanRepository productPlanRepository;
    @Autowired MaterialInOutEntityRepository meterialRepository;
    @Autowired SalesRepository salesRepository;
    @Autowired AllowApprovalRepository allowApprovalRepository;

    // 0. 제네릭 사용하기 위해 생성
    public List<?> getEntityListByType(int type) {
        if (type == 1) {
                log.info( "getEntityListByType" + meterialRepository.findAll().toString());
            return meterialRepository.findAll();
        } else if (type == 2) {
                log.info( "getEntityListByType" + productPlanRepository.findAll().toString());
            return productPlanRepository.findAll();
        } else if (type == 3) {
                log.info( "getEntityListByType" + salesRepository.findAll().toString());
            return salesRepository.findAll();
        } else{
            throw new IllegalArgumentException("알 수 없는 요청");
        }
    }

    // 1. 승인 요청 데이터 출력 [type: 1 - 자재, 2 - 제품, 3 - 판매 ]
    // *프론트 처리 필요 사항: option: 1 - 미승인, 2 - 승인, 3 - 전체 출력 (Back에서 관리하면 로직 복잡해짐)
    public List<?> printAllowApproval( int type ){

        // 2. 승인 리스트 가져오기 [제네릭 사용 시도 - 3가지 타입 한번에 받기 위함]
        List<?> approvalList = getEntityListByType(type);

        // 3. 승인 리스트 저장소 생성
        List<Object> result;
        // 의견: Object 물음표로 변경해도 문제 없는지 테스트 필요

        // 4. 결재권자인 경우, 아래 내용 출력 [type 별로 List 저장 후 출력]
        if( type == 1) { // 자재
            result = new ArrayList<>();
            for (Object obj : approvalList) {
                MaterialInOutEntity entity = (MaterialInOutEntity) obj;

                MaterialInOutDto dto = new MaterialInOutDto(entity.getMat_in_outid(), entity.getMat_in_type(), entity.getMat_st_stock(),
                        entity.cdate.toLocalDate(), entity.udate.toLocalDate(), entity.getAllowApprovalEntity().toInDto(),
                        entity.getMaterialEntity().toDto(), entity.getMemberEntity().toDto()
                );
                result.add(dto);
            }

        } else if ( type == 2) { // 제품
            result = new ArrayList<>();
            for (Object obj : approvalList) {
                ProductPlanEntity entity = (ProductPlanEntity) obj;
                ProductPlanDto dto = new ProductPlanDto(
                        entity.getProdPlanNo(), entity.getProdPlanCount(), entity.getProdPlanDate(), entity.getProductEntity(), entity.getAllowApprovalEntity());
                result.add(dto);
            }
        } else if ( type == 3) { // 판매
            result = new ArrayList<>();
            for (Object obj : approvalList) {
                SalesEntity entity = (SalesEntity) obj;
                System.out.println(entity);
                SalesDto dto = new SalesDto(
                        entity.getOrder_id(), entity.getOrderDate(),
                        entity.getOrderCount(), entity.getOrder_status(), entity.getSalesPrice(),
                        entity.getAllowApprovalEntity().toInDto(), entity.getCompanyEntity().toDto(), entity.getProductEntity().toDto(), entity.getMemberEntity().toDto());
                result.add(dto);
            }
        } else{ // 예외 처리(PermissionDeniedException 클래스 공용 사용)
            throw new PermissionDeniedException("알 수 없는 요청");
        }
            log.info("printAllowApproval: " + result);
        return result;
    }
    // 예상되는 문제점: repository null이면 에러 발생할 거 같음 (확인 필요)

    // 2. 승인/반려 처리 (코드 중복 최소화 - 동일 작동 메소드 생성)
    private boolean updateAllowApproval(int id, boolean approval, HttpSession session) {

        Optional<AllowApprovalEntity> allowApprovalEntity = allowApprovalRepository.findById(id);
        if (allowApprovalEntity.isPresent()) {
            AllowApprovalEntity entity = allowApprovalEntity.get();
            entity.setAl_app_whether(approval);
            entity.setAl_app_date(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            MemberEntity member = (MemberEntity) session.getAttribute("member");
            entity.setMemberEntity(member);
            allowApprovalRepository.save(entity);
            return approval; // 코드 최소화하는 방법으로 판단되어 사용
        }
        return false;
        // 로직 처리가 안되어도 false로 반환되는 문제점이 있음
    }

    // 3. 자재, 제품, 판매 승인/반려 처리 (type에 맞게 updateAllowApproval 메소드에 분배하는 역할)
    public boolean approveMaterialInOut(List<Integer> MatInOutIDs, HttpSession session ) {
            System.out.println("approveMaterialInOut");
            System.out.println(MatInOutIDs.toString());
        for (int id : MatInOutIDs) {
            updateAllowApproval(id, true, session);
        }
        return true;
    }
    public boolean rejectMaterialInOut(List<Integer> MatInOutIDs, HttpSession session) {
        for (int id : MatInOutIDs) {
            updateAllowApproval(id, false, session);
        }
        return true;
    }
    public boolean approveProductInOut(List<Integer> ProdInOutIDs, HttpSession session) {
        for (int id : ProdInOutIDs) {
            updateAllowApproval(id, true, session);
        }
        return true;
    }
    public boolean rejectProductInOut(List<Integer> ProdInOutIDs, HttpSession session) {
        for (int id : ProdInOutIDs) {
            updateAllowApproval(id, false, session);
        }
        return true;
    }
    public boolean approveSales(List<Integer> OrderIds, HttpSession session) {
        for (int id : OrderIds) {
            updateAllowApproval(id, true, session);
        }
        return true;
    }
    public boolean rejectSales(List<Integer> OrderIds, HttpSession session) {
        for (int id : OrderIds) {
            updateAllowApproval(id, false, session);
        }
        return true;
    }
}