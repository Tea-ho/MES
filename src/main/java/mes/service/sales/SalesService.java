package mes.service.sales;

import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.ProductProcessRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.member.AllowApprovalDto;
import mes.domain.dto.member.CompanyDto;
import mes.domain.dto.product.ProductDto;
import mes.domain.dto.product.ProductProcessDto;
import mes.domain.dto.sales.ProductProcessPageDto;
import mes.domain.dto.sales.SalesDto;
import mes.domain.dto.sales.SalesPageDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.member.*;
import mes.domain.entity.product.ProductEntity;
import mes.domain.entity.product.ProductProcessEntity;
import mes.domain.entity.sales.SalesEntity;
import mes.domain.entity.sales.SalesRepository;
import mes.service.product.AutoProduceService;
import mes.service.product.ProductProcessService;
import mes.webSocket.ChattingHandler;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.TextMessage;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    MemberRepository memberRepository;

    @Autowired
    AllowApprovalRepository allowApprovalRepository;

    @Autowired
    AutoProduceService autoProduceService; //제품 자동 생성을 위해

    @Autowired
    private ChattingHandler chattingHandler;

    // 0. 판매 공정 product_process 출력
    public ProductProcessPageDto getProductProcess(ProductProcessPageDto productProcessPageDto){
        List<ProductProcessDto> list = new ArrayList<>();


            Pageable pageable = PageRequest.of(productProcessPageDto.getPage()-1 , 5 , Sort.by(Sort.Direction.DESC , "prod_stock"));

            Page<ProductProcessEntity> productProcessEntityPage = productProcessRepository.findByPage(productProcessPageDto.getKeyword() , pageable);
            productProcessEntityPage.forEach((e)->{
                list.add(e.toDto());
            });

            productProcessPageDto.setProductProcessDtoList(list);
            productProcessPageDto.setTotalPage(productProcessEntityPage.getTotalPages());
            productProcessPageDto.setTotalCount(productProcessEntityPage.getTotalElements());


        System.out.println("productProcessPageDto : " + productProcessPageDto);

        return productProcessPageDto;

    }

    // 1. 판매 등록
    @Transactional
    public boolean salesCreate (SalesDto salesDto){

        // 회사 cno
        CompanyEntity companyEntity = companyRepository.findById( salesDto.getCno()).get();

        // 물건 id
        ProductEntity productEntity = productRepository.findById( salesDto.getProdId()).get();
        System.out.println("productEntity : " + productEntity);

        MemberEntity member = memberRepository.findByMnameAndMpassword(salesDto.getMemberDto().getMname() , salesDto.getMemberDto().getMpassword() );

        // 승인정보
        AllowApprovalEntity approvalEntity = allowApprovalRepository.save(new AllowApprovalDto().toInEntity());

        SalesEntity salesEntity = salesRepository.save( salesDto.toEntity() );
        salesEntity.setAllowApprovalEntity(approvalEntity);         // 승인 정보 저장
        salesEntity.setCompanyEntity(companyEntity);                // 회사 저장
        salesEntity.setProductEntity( productEntity );              // 물품 저장

        salesEntity.setOrder_status(0);                             // 기본 등록 order_status = 0

        salesEntity.setMemberEntity(member);                        // 멤버 저장

        log.info("salesEntity : " + salesEntity);

        // 채팅 소켓 판매쪽 : 30번
        try{
            chattingHandler.handleMessage(null , new TextMessage("30"));

        }catch (Exception e){
            System.out.println(e);
        }

        if ( salesEntity.getOrder_id() >= 1 ) {
            return true;
        }

        return false;

    }



    // [등록 조건1] ctype 2인 회사불러오기
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


    // 2. 판매 출력
    @Transactional
    public SalesPageDto salesView(SalesPageDto salesPageDto){
        List<SalesDto> list = new ArrayList<>();

        if(salesPageDto.getOrder_id() == 0){
            Pageable pageable = PageRequest.of(salesPageDto.getPage()-1 , 5 , Sort.by(Sort.Direction.DESC , "order_id"));

            Page<SalesEntity> entityPage = salesRepository.findByPage(salesPageDto.getKeyword() , pageable);
            entityPage.forEach((e)->{
                list.add(e.toDto());
            });

            salesPageDto.setSalesDtoList(list);
            salesPageDto.setTotalPage(entityPage.getTotalPages());
            salesPageDto.setTotalCount(entityPage.getTotalElements());

        }
        else if(salesPageDto.getOrder_id() >= 0){
            SalesEntity entity = salesRepository.findById(salesPageDto.getOrder_id()).get();

            list.add(entity.toDto());
            salesPageDto.setSalesDtoList(list);
        }
        System.out.println("salesPageDto : " + salesPageDto);

        return salesPageDto;

    }

    // 3. 판매 수정
    @Transactional
    public boolean SalesUpdate( SalesDto salesDto){
        SalesEntity salesEntity = salesRepository.findById(salesDto.getOrder_id()).get();

        CompanyEntity companyEntity = companyRepository.findById(salesDto.getCno()).get();
        salesEntity.setCompanyEntity(companyEntity);            // 회사 수정


        ProductEntity productEntity = productRepository.findById( salesDto.getProdId()).get();
        salesEntity.setProductEntity( productEntity );          // 물품 저장

        log.info("productEntity 수정된 prodid번호 : " + productEntity.getProdId());

        salesEntity.setOrderCount(salesDto.getOrderCount());    // 개수 수정
        salesEntity.setSalesPrice(salesDto.getSalesPrice());    // 가격 수정

        log.info("수정 처리 salesEntity : " + salesEntity);

        salesRepository.save(salesEntity);

        return true;
    }

    // 4. 판매 삭제
    @Transactional
    public boolean SalesDelete(int order_id){
        Optional<SalesEntity> salesEntity = salesRepository.findById(order_id);
        if ( salesEntity.isPresent() ){
            allowApprovalRepository.delete(salesEntity.get().getAllowApprovalEntity());
            salesRepository.delete(salesEntity.get());
            return true;
        }
        return false;
    }

    // 5. 판매 stock 바꾸기
    @Transactional
    public boolean SalesStock(SalesDto salesDto){

        Optional<SalesEntity> salesEntity = salesRepository.findOrder(salesDto.getOrder_id());

        SalesEntity AllowId = salesRepository.findByAllowId(salesDto.getAl_app_no());

        ProductEntity productEntity = productRepository.findById(salesDto.getProdId()).get();

        ProductProcessEntity productProcessEntities  = productProcessRepository.findByProdId(productEntity.getProdId());



        int baseStock = productProcessEntities.getProdStock();
            log.info("baseStock : " + baseStock);
        int salesStock = salesEntity.get().getOrderCount();
            log.info("salesStock : " + salesStock);
        if ( baseStock - salesStock < 0){
            return false;
        }
        int updateStock = baseStock - salesStock;
        productProcessEntities.setProdStock(updateStock);

        AllowId.setOrder_status(2);
        salesRepository.save(AllowId);
        productProcessRepository.save(productProcessEntities);

        //판매처리가 되면 제품별 자동 생성 (재고 안정성을 위해)
        autoProduceService.AutoProduce(salesDto.getProdId());
        return true;
    }
}