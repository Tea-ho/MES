package mes.service.performance;

import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.ProductPlanRepository;
import mes.domain.dto.product.ProductProductionByMonthDto;
import mes.domain.dto.product.ProductProductionByQuarterDto;
import mes.domain.dto.product.ProductProductionByYearDto;
import mes.domain.dto.product.ProductProductionDto;
import mes.domain.dto.sales.SalesByCompanyDto;
import mes.domain.dto.sales.SalesByMemberDto;
import mes.domain.dto.sales.SalesByProductDto;
import mes.domain.entity.sales.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service@Slf4j
public class PerformanceService { // 실적(생산/판매) 조회

    @Autowired private ProductPlanRepository productPlanRepository;
    @Autowired private SalesRepository salesRepository;

    public List<?> getProductionDto(int type){ // 제품 생산실적
        if (type == 1) {
                return productPlanRepository.findProductProduction();
        } else if (type == 2) {
                return productPlanRepository.findProductProductionByMonth();
        } else if (type == 3) {
                return productPlanRepository.findProductProductionByQuarter();
        } else if (type == 4) {
                return productPlanRepository.findProductProductionByYear();
        } else {
            throw new IllegalArgumentException("알 수 없는 요청");
        }
    }

    public List<?> getSalesDto(int type, int id){
        if (type == 1) {
            return salesRepository.findSalesByProduct(id);
        } else if (type == 2) {
            return salesRepository.findSalesByMember(id);
        } else if (type == 3) {
            return salesRepository.findSalesByCompany(id);
        } else if (type == 4) {
            return productPlanRepository.findProductProductionByYear();
        } else {
            throw new IllegalArgumentException("알 수 없는 요청");
        }
    }
}