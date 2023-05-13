package mes.controller.performance;

import lombok.extern.slf4j.Slf4j;
import mes.service.performance.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/perform")
public class PerformanceController {
    @Autowired private PerformanceService performanceService;

    // 1. 생산 실적 출력
    @GetMapping("/prdocution")
    public List<?> printProduction(@RequestParam int type){
            log.info("printProduction type:"+type);
        try{
            return performanceService.getProductionDto(type);
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
    // 2. 판매 실적 출력
    @GetMapping("/sales")
    public List<?> printSales(@RequestParam int type, @RequestParam int id) {
            log.info("printSales type:"+type);
        try {
            return performanceService.getSalesDto(type, id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
