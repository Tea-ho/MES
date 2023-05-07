package mes.controller;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.sales.SalesDto;
import mes.service.sales.SalesService;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    SalesService salesService;

    // 1. 판매 등록 ( 승인이 되었을 경우, 제품 재고량이 줄어들어야함 )
    @PostMapping("/salesCreate")
    public boolean salesCreate( @RequestBody SalesDto salesDto ){
        log.info("salesCreate : " + salesDto);

        return salesService.salesCreate( salesDto );
    }

    // 2. 판매 출력 [ 출력창 2개 필요 , 승인 전 = 수정, 삭제 가능 , 승인 후 = 수정 삭제 불가 ]
    @GetMapping("/salesView")
    public List<SalesDto> salesView(@RequestParam int OrderId){
        return salesService.salesView(OrderId);

    }

    // 3. 판매 수정 ( 판매 수정했을 경우, 제품 재고량이 다시 늘어나야 함 ) / 결제자 승인 허락 전


    // 4. 판매 삭제 ( 판매 삭제했을 경우, 제품 재고량이 다시 늘어나야 함 ) / 결제자 승인 허락 전

}
