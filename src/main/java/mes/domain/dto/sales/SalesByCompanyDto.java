package mes.domain.dto.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data@NoArgsConstructor@AllArgsConstructor@Builder
public class SalesByCompanyDto { // 구매처별 판매실적 DTO
    private int salesPrice;
    private String cname;
}
