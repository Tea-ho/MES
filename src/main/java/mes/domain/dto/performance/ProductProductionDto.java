package mes.domain.dto.performance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor@AllArgsConstructor@Builder
public class ProductProductionDto {
    private String productName;
    private long prodPrice;
    private long averageProductionCount;
    private long totalProductionCount;
    private long totalProductionAmount;
    private double productionPercentage;
}
