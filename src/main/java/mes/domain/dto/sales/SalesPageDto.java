package mes.domain.dto.sales;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesPageDto {
    private long totalCount;
    private int totalPage;

    private int page;
    private String key;
    private String keyword;

    List<SalesDto> salesDtoList;

}
