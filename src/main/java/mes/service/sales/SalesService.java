package mes.service.sales;

import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.ProductProcessRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.member.AllowApprovalDto;
import mes.domain.dto.member.CompanyDto;
import mes.domain.dto.product.ProductDto;
import mes.domain.dto.sales.SalesDto;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.AllowApprovalRepository;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.CompanyRepository;
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


    // 1. 판매 등록
    @Transactional
    public boolean salesCreate (SalesDto salesDto){

        // 회사 cno
        CompanyEntity companyEntity = companyRepository.findById( salesDto.getCno()).get();

        // 물건 id
        ProductEntity productEntity = productRepository.findById( salesDto.getProdId()).get();
        System.out.println("productEntity : " + productEntity);


        // * 등록 시에 재고량 불러와서 감소 기능 필요 *
        ProductProcessEntity productProcessEntity = productProcessRepository.findById( salesDto.getProductProcessEntity().getProdStock() ).get();



        int currentStock = productProcessEntity.getProdStock(); // 현재 있는 제품 개수
        System.out.println("currentStock : " + currentStock );
        int salesStock = salesDto.getOrderCount();              // 판매하려는 제품 개수
        System.out.println("salesStock : " + salesStock );
        if ( currentStock - salesStock < 0){                    // 개수 유효성 검사( 0보다 작아지지 않게 )
            return false;
        }

        int updateStock = currentStock - salesStock;
        productProcessEntity.setProdStock(updateStock);         // 판매 성공 성공 시에 개수 줄어들게하기*/

        // 승인정보
        AllowApprovalEntity approvalEntity = allowApprovalRepository.save(new AllowApprovalDto().toInEntity());

        SalesEntity salesEntity = salesRepository.save( salesDto.toEntity() );
        salesEntity.setCompanyEntity(companyEntity);
        salesEntity.setProductEntity( productEntity );


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

    // [등록 조건1] ctype 2인 회사불러오기 ( react 에서 처리 )
    @Transactional
    public List<CompanyDto> getCompany( ) {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        List<CompanyEntity> entityList = companyRepository.findAll();

        entityList.forEach((e) -> {
            companyDtoList.add(e.toDto());
        });
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
