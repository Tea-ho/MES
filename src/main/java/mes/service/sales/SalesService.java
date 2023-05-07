package mes.service.sales;

import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.ProductProcessRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.sales.SalesDto;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductProcessEntity;
import mes.domain.entity.sales.SalesEntity;
import mes.domain.entity.sales.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SalesService {

    @Autowired
    SalesRepository salesRepository;

    @Autowired
    ProductProcessRepository productProcessRepository;
    // 재고량, status 빼오기 위함(공정 상태)

    @Autowired
    ProductRepository productRepository;
    // prod_Id or prod_name 빼오기 위함

    // 1. 판매 등록
    @Transactional
    public boolean salesCreate (SalesDto salesDto){
        // 물건 id
        ProductEntity productEntity = productRepository.findById( salesDto.getProdId()).get();
        System.out.println("productEntity : " + productEntity);

/*
        // 재고량
        ProductProcessEntity productProcessEntity = productProcessRepository.findById( salesDto.getProdStock() ).get();
*/

        SalesEntity salesEntity = salesRepository.save( salesDto.toEntity() );
        System.out.println("salesEntity : " + salesEntity);

        salesEntity.setProductEntity( productEntity );
/*
        salesEntity.setOrderCount(productProcessEntity.getProdStock());
*/

        log.info("Sales entity : " + salesEntity);
        if ( salesEntity.getOrderId() >= 1 ) { return true; }

        return false;

    }

    // 2. 판매 출력 1( 판매 승인 전 )
    @Transactional
    public List<SalesDto> salesView(int OrderId){
        List<SalesDto> list = new ArrayList<>();
        if ( OrderId == 0 ){
            List<SalesEntity> salesEntities = salesRepository.findAll();
            salesEntities.forEach( (e) -> {
                list.add(e.toDto());
            });
        } else if( OrderId > 0 ){
            SalesEntity salesEntity = salesRepository.findById(OrderId).get();
            list.add(salesEntity.toDto());
        }
        return list;
    }

}
