package mes.controller.member;

import lombok.extern.slf4j.Slf4j;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.PermissionDeniedException;
import mes.service.Material.MaterialService;
import mes.service.member.AllowApprovalService;
import mes.service.product.ProductService;
import mes.service.sales.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/allowApproval")
public class AllowApprovalController {

    @Autowired private AllowApprovalService allowApprovalService;

    // 1. 승인 리스트 출력
    @GetMapping("")
    public List<?> printAllowApproval(@RequestParam int type, HttpSession session) {

        // 1. 승인권자 권한 확인 (제어)
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (!member.getPosition().equals("임원")) { // 아니면 던지기 처리 (PermissionDeniedException 예외 클래스 추가 생성)
            throw new PermissionDeniedException("권한이 없습니다.");
        } else{
            try { log.info(allowApprovalService.printAllowApproval(type).toString());
                return allowApprovalService.printAllowApproval(type);
            } catch (PermissionDeniedException e) {
                // 권한 없음 예외 처리
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
            } catch (Exception e) {
                // 그 외 예외 처리
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            }
        }
    }

    // 2. 승인 처리
    @PutMapping("") // type 1: 자재, 2: 제품 , 3: 판매 // approve 1: 승인, 2: 반려
    public boolean updateAllowApproval(@RequestParam int type, @RequestParam int approve, @RequestParam int id ){
        if(type == 1){
            if( approve == 1 ){
                return allowApprovalService.approveMaterialInOut(id);
            } else{ return allowApprovalService.rejectMaterialInOut(id); }
        } else if(type == 2){
            if( approve == 1 ){
                return allowApprovalService.approveProductInOut(id);
            } else{ return allowApprovalService.rejectProductInOut(id); }
        } else{
            if( approve == 1 ){
                return allowApprovalService.approveSales(id);
            } else{ return allowApprovalService.rejectSales(id); }
        }
    }
}
