package mes.service.member;

import mes.domain.entity.material.MaterialInOutEntity;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.PermissionDeniedException;
import mes.domain.entity.product.ProductPlanEntity;
import mes.domain.entity.sales.SalesEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class AllowApprovalService {

    @Autowired ProductPlanRepository productPlanRepository;
    @Autowired MeterialRepository meterialRepository;
    @Autowired SalesRepository salesRepository;

    // 1. 출력( 뭘 출력? )

    // 2. 승인/반려 처리
    // -------------------------- 제품 --------------------------
    public void approveProductPlan(int prodPlanNo, HttpSession session) { // --- 제품 생산 승인
        
        // 1. 승인권자 권한 확인
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (!member.getPosition().equals("임원")) { // 아니면 던지기 처리 (PermissionDeniedException 예외 클래스 추가 생성)
            throw new PermissionDeniedException("권한이 없습니다.");
        }
        // 2. productPlanEntity 찾기
        Optional<ProductPlanEntity> productPlanEntity = productPlanRepository.findById(prodPlanNo);

        // 3. 존재하는 경우, productPlanEntity의 AllowApprovalEntity 업데이트 처리//
        if (productPlanEntity.isPresent()) {
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
