package mes.service.sales;

import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.ProductProcessRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.member.AllowApprovalDto;
import mes.domain.dto.member.CompanyDto;
import mes.domain.dto.product.ProductDto;
import mes.domain.dto.sales.SalesDto;
import mes.domain.dto.sales.SalesPageDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.AllowApprovalRepository;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.CompanyRepository;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductProcessEntity;
import mes.domain.entity.sales.SalesEntity;
import mes.domain.entity.sales.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SalesService {

    @Autowired
    CompanyRepository companyRepository; //회사 ctype 2인 회사만 빼오기 위함

    @Autowired
    SalesRepository salesRepository;

    @Autowired
    ProductProcessRepository productProcessRepository;
    // 재고량, status 빼오기 위함(공정 상태)

    @Autowired
    ProductRepository productRepository;
    // prod_Id or prod_name 빼오기 위함

    @Autowired
    AllowApprovalRepository allowApprovalRepository;

    // 0. 판매 쪽 product_process 출력


    // 1. 판매 등록
    @Transactional
    public boolean salesCreate (SalesDto salesDto){

        // 회사 cno
        CompanyEntity companyEntity = companyRepository.findById( salesDto.getCno()).get();

        // 물건 id
        ProductEntity productEntity = productRepository.findById( salesDto.getProdId()).get();
        System.out.println("productEntity : " + productEntity);

        // * 등록 시에 재고량 불러와서 감소 기능 필요 *
/*      ProductProcessEntity productProcessEntity = productProcessRepository.findStockByProductId( productEntity.getProdId() );

        int currentStock = productProcessEntity.getProdStock();
        int salesStock = salesDto.getOrderCount();              // 판매하려는 제품 개수
        if ( currentStock - salesStock < 0){                    // 개수 유효성 검사( 0보다 작아지지 않게 )
            return false;
        }
        int updateStock = currentStock - salesStock;
        productProcessEntity.setProdStock(updateStock);         // 판매 성공 성공 시에 개수 줄어들게하기*/


        // 승인정보
        AllowApprovalEntity approvalEntity = allowApprovalRepository.save(new AllowApprovalDto().toInEntity());

        SalesEntity salesEntity = salesRepository.save( salesDto.toEntity() );
        salesEntity.setAllowApprovalEntity(approvalEntity);
        salesEntity.setCompanyEntity(companyEntity);
        salesEntity.setProductEntity( productEntity );


        log.info("Sales entity : " + salesEntity);
        if ( salesEntity.getOrder_id() >= 1 ) { return true; }

        return false;

    }

    // 2. 판매 출력
    @Transactional
    public SalesPageDto salesView(SalesPageDto salesPageDto){
        List<SalesDto> list = new ArrayList<>();


        if(salesPageDto.getOrderStatus() == 0){
            Pageable pageable = PageRequest.of(salesPageDto.getPage()-1 , 5 , Sort.by(Sort.Direction.DESC , "order_id"));

            Page<SalesEntity> entityPage = salesRepository.findByPage(salesPageDto.getKeyword() , pageable);
            entityPage.forEach((e)->{
                list.add(e.toDto());
            });

            salesPageDto.setSalesDtoList(list);
            salesPageDto.setTotalPage(entityPage.getTotalPages());
            salesPageDto.setTotalCount(entityPage.getTotalElements());
        }
        else if(salesPageDto.getOrderStatus() > 0){
            SalesEntity entity = salesRepository.findById(salesPageDto.getOrderStatus()).get();
            list.add(entity.toDto());

            salesPageDto.setSalesDtoList(list);
        }
        System.out.println("Servicedto : " + salesPageDto);

        return salesPageDto;

    }

    // [등록 조건1] ctype 2인 회사불러오기 ( react 에서 처리 )
    @Transactional
    public List<CompanyDto> getCompany( ) {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        List<CompanyEntity> entityList = companyRepository.findAll();

        entityList.stream()
                .filter(e -> e.getCtype() == 2)
                .forEach(e -> companyDtoList.add(e.toDto()));
        return companyDtoList;
    }

    // [등록 조건2 ] 물품 불러오기
    @Transactional
    public List<ProductDto> getProduct(){
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductEntity> entityList = productRepository.findAll();

        entityList.forEach((e) -> {
            productDtoList.add(e.toDto());
        });
        return productDtoList;
    }

}
