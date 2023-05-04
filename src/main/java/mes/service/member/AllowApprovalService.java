/*
package mes.service.member;

import mes.domain.Repository.product.ProductPlanRepository;
import mes.domain.dto.material.MaterialInOutDto;
import mes.domain.dto.product.ProductPlanDto;
import mes.domain.dto.sales.SalesDto;
import mes.domain.entity.material.MaterialInOutEntity;
import mes.domain.entity.material.MaterialInOutEntityRepository;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.PermissionDeniedException;
import mes.domain.entity.product.ProductPlanEntity;
import mes.domain.entity.sales.SalesEntity;
import mes.domain.entity.sales.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

public class AllowApprovalService {

    @Autowired
    ProductPlanRepository productPlanRepository;
    @Autowired
    MaterialInOutEntityRepository meterialRepository;
    @Autowired
    SalesRepository salesRepository;

    // 0. 제네릭 사용하기 위해 생성
    // 항상 인수를 받기 때문에 optional 필요 없음
    public Optional<List<?>> getEntityListByType(int type) {
        if (type == 1) {
            return Optional.of(productPlanRepository.findAll());
        } else if (type == 2) {
            return Optional.of(meterialRepository.findAll());
        } else if (type == 3) {
            return Optional.of(salesRepository.findAll());
        } else {
            return Optional.empty();
        }
    }

    // 1. 승인 요청 데이터 출력 [type: 1 - 자재, 2 - 제품, 3 - 판매 ]
    // *프론트 처리 필요 사항: option: 1 - 미승인, 2 - 승인, 3 - 전체 출력 (Back에서 관리하면 로직 복잡해짐)
    public List<?> printAllowApproval(HttpSession session, int type){

        // 1. 승인권자 권한 확인 (제어)
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (!member.getPosition().equals("임원")) { // 아니면 던지기 처리 (PermissionDeniedException 예외 클래스 추가 생성)
            throw new PermissionDeniedException("권한이 없습니다.");
        }

        // 2. 승인 리스트 가져오기 [제네릭 사용 시도 - 3가지 타입 한번에 받기 위함]
        Optional<List<?>> approvalListOptional = Optional.ofNullable(getEntityListByType(type));
        // 해석: Null 값 여부 확인
        List<?> approvalList = approvalListOptional.orElseThrow(
                () -> new PermissionDeniedException("알 수 없는 요청"));
        // 해석: approvalListOptional null이 아니면 List를 반환 & null이면 예외처리
        
        // 3. 승인 리스트 저장소 생성
        List<Object> result = new ArrayList<>();
        // 의견: Object 물음표로 변경해도 문제 없는지 테스트 필요

        // 4. 결재권자인 경우, 아래 내용 출력 [type 별로 List 저장 후 출력]
        if( type == 1) { // 자재
            for (Object obj : approvalList) {
                ProductPlanEntity entity = (ProductPlanEntity) obj;
                ProductPlanDto dto = new ProductPlanDto(
                        entity.getProdPlanNo(), entity.getProdPlanCount(), entity.getProdPlanDate(), entity.getProductEntity(), entity.getAllowApprovalEntity());
                result.add(dto);
            }

        } else if ( type == 2) { // 제품
            for (Object obj : approvalList) {
                MaterialInOutEntity entity = (MaterialInOutEntity) obj;
               */
/* MaterialInOutDto dto = new MaterialInOutDto(
                        entity.getMatInOutID(), entity.getMatInType(),
                        entity.getMatStStock(), entity.getAllowApprovalEntity(), entity.getMaterialEntity());
                result.add(dto);*//*

            }
        } else if ( type == 3) { // 판매
            for (Object obj : approvalList) {
                SalesEntity entity = (SalesEntity) obj;
                SalesDto dto = new SalesDto(
                        entity.getOrderId(), entity.getOrderDate(),
                        entity.getOrderCount(), entity.getOrderStatus(), entity.getSalesPrice(),
                        entity.getAllowApprovalEntity(), entity.getCompanyEntity(), entity.getProductEntity(), entity.getMemberEntity());
                result.add(dto);
            }
        } else{ // 예외 처리(PermissionDeniedException 클래스 공용 사용)
            throw new PermissionDeniedException("알 수 없는 요청");
        }
        return result;
    }

    // 2. 승인/반려 처리
    // -------------------------- 제품 --------------------------
    public void approveProductPlan(HttpSession session, int prodPlanNo) { // --- 제품 생산 승인
        
        // 1. 승인권자 권한 확인
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (!member.getPosition().equals("임원")) { // 아니면 던지기 처리 (PermissionDeniedException 예외 클래스 추가 생성)
            throw new PermissionDeniedException("권한이 없습니다.");
        }
        // 2. productPlanEntity 찾기
        Optional<ProductPlanEntity> productPlanEntity = productPlanRepository.findById(prodPlanNo);

        // 3. 존재하는 경우, productPlanEntity의 AllowApprovalEntity 업데이트 처리//
        if(productPlanEntity.isPresent()){
            ProductPlanEntity plan = productPlanEntity.get();
            plan.getAllowApprovalEntity().setAlAppWhether(true);
            plan.getAllowApprovalEntity().setAlAppDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            productPlanRepository.save(plan);
        }
    }

    public void rejectProductPlan(int prodPlanNo, HttpSession session ) { // --- 제품 생산 반려

        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (!member.getPosition().equals("임원")) {
            throw new PermissionDeniedException("권한이 없습니다.");
        }

        Optional<ProductPlanEntity> productPlanEntity = productPlanRepository.findById(prodPlanNo);
        if (productPlanEntity.isPresent()) {
            ProductPlanEntity plan = productPlanEntity.get();
            plan.getAllowApprovalEntity().setAlAppWhether(false);
            plan.getAllowApprovalEntity().setAlAppDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            productPlanRepository.save(plan);
        }
    }

    // -------------------------- 자재 --------------------------
    public void approveMaterialInOut(int MatInOutID, HttpSession session ) { // --- 자재 생산 승인

        // 1. 승인권자 권한 확인
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (!member.getPosition().equals("임원")) { // 아니면 던지기 처리 (PermissionDeniedException 예외 클래스 추가 생성)
            throw new PermissionDeniedException("권한이 없습니다.");
        }

        Optional<MaterialInOutEntity> materialInOutEntity = meterialRepository.findById(MatInOutID);
        if (materialInOutEntity.isPresent()) {
            MaterialInOutEntity inOut = materialInOutEntity.get();
            inOut.getAllowApprovalEntity().setAlAppWhether(true);
            inOut.getAllowApprovalEntity().setAlAppDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            meterialRepository.save(inOut);
        }
    }

    public void rejectMaterialInOut(int MatInOutID, HttpSession session) { // --- 자재 생산 반려

        // 1. 승인권자 권한 확인
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (!member.getPosition().equals("임원")) { // 아니면 던지기 처리 (PermissionDeniedException 예외 클래스 추가 생성)
            throw new PermissionDeniedException("권한이 없습니다.");
        }

        Optional<MaterialInOutEntity> materialInOutEntity = meterialRepository.findById(MatInOutID);
        if (materialInOutEntity.isPresent()) {
            MaterialInOutEntity inOut = materialInOutEntity.get();
            inOut.getAllowApprovalEntity().setAlAppWhether(false);
            inOut.getAllowApprovalEntity().setAlAppDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            meterialRepository.save(inOut);
        }
    }

    // -------------------------- 판매 --------------------------
    public void approveSales(int OrderId, HttpSession session) { // --- 판매 승인

        // 1. 승인권자 권한 확인
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (!member.getPosition().equals("임원")) { // 아니면 던지기 처리 (PermissionDeniedException 예외 클래스 추가 생성)
            throw new PermissionDeniedException("권한이 없습니다.");
        }

        Optional<SalesEntity> salesEntity = salesRepository.findById(OrderId);
        if (salesEntity.isPresent()) {
            SalesEntity inOut = salesEntity.get();
            inOut.getAllowApprovalEntity().setAlAppWhether(true);
            inOut.getAllowApprovalEntity().setAlAppDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            salesRepository.save(inOut);
        }
    }

    public void rejectSales(int OrderId, HttpSession session) { // --- 판매 반려

        // 1. 승인권자 권한 확인
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (!member.getPosition().equals("임원")) { // 아니면 던지기 처리 (PermissionDeniedException 예외 클래스 추가 생성)
            throw new PermissionDeniedException("권한이 없습니다.");
        }

        Optional<SalesEntity> salesEntity = salesRepository.findById(OrderId);
        if (salesEntity.isPresent()) {
            SalesEntity inOut = salesEntity.get();
            inOut.getAllowApprovalEntity().setAlAppWhether(false);
            inOut.getAllowApprovalEntity().setAlAppDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            salesRepository.save(inOut);
        }
    }
    // 3.

}
*/
