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

import java.util.List;

@Service@Slf4j
public class PerformanceService {

    @Autowired ProductPlanRepository productPlanRepository;
    @Autowired SalesRepository salesRepository;

    // 1. 생산 실적 (제품별로 나누지 않았음)
    public List<ProductProductionDto> getProductProduction(){
        return productPlanRepository.findProductProduction();
    }
    public List<ProductProductionByMonthDto> getProductProductionByMonth(){
        return productPlanRepository.findProductProductionByMonth();
    }
    public List<ProductProductionByQuarterDto> getProductProductionByQuarter(){
        return productPlanRepository.findProductProductionByQuarter();
    }
    public List<ProductProductionByYearDto> getProductProductionByYear() {
        return productPlanRepository.findProductProductionByYear();
    }
    // 2. 판매 실적
    public List<SalesByProductDto> getSalesByProduct(int productId) {
        return salesRepository.findSalesByProduct(productId);
    }
    public List<SalesByMemberDto> getSalesByMember(int memberId) {
        return salesRepository.findSalesByMember(memberId);
    }
    public List<SalesByCompanyDto> getSalesByCompany(int companyId){
        return salesRepository.findSalesByCompany(companyId);
    }
}
