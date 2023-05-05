package mes.service.product;

import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.MaterialProductRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.product.MaterialProductDto;
import mes.domain.dto.product.PageDto;
import mes.domain.dto.product.ProductDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.member.CompanyEntity;
import mes.domain.entity.member.CompanyRepository;
import mes.domain.entity.product.MaterialProductEntity;
import mes.domain.entity.product.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    MaterialProductRepository materialProductRepository;

    @Autowired
    MaterialEntityRepository materialEntityRepository;

    //제품 출력 => 제품 지시, 제품 관리 페이지에서 수행할 예정
    public PageDto getProductList(PageDto pageDto){
        Pageable pageable = PageRequest.of(pageDto.getPage()-1, 5, Sort.by(Sort.Direction.DESC, "prodId") );
        
        //페이징 처리를 위해 해당 키와 키워드 페이지 5개씩 지정
        Page<ProductEntity> pageEntity = productRepository.findBySearch(pageable, pageDto.getKey(), pageDto.getKeyword());

        List<ProductDto> productDto = new ArrayList<ProductDto>();
        
        //해당 pageEntity는 필요한 정보값(제품)을 가지고 있는 리스트이다.
        pageEntity.forEach((p) -> {
            /*CompanyEntity companyEntity = companyRepository.findById(p.getCompanyEntity().getCno()).get();*/
            productDto.add(p.toDto());
        });

        pageDto.setProductDtoList(productDto);
        pageDto.setTotalPage(pageEntity.getTotalPages());
        pageDto.setTotalCount(pageEntity.getTotalElements());

        return pageDto;
    }

    //제품 등록 => 제품 생산페이지에서 수행 할 예정
    public boolean postProduct(ProductDto productDto){

        List<MaterialEntity> materialEntityList = new ArrayList<>(); //자재 PK번호로 해당

        for(int i = 0; i < productDto.getMaterialList().size(); i++){
            materialEntityList.add(materialEntityRepository.findById(productDto.getMaterialList().get(i)).get());
        }

        System.out.println("자재 조회 : " + materialEntityList);

        ProductEntity productEntity = productRepository.save(productDto.toEntity());

        if(productEntity.getProdId() < 1){ //앞부분 등록 실패시
            return false;
        }

        System.out.println("제품 등록 후 " + productEntity);

        MaterialProductEntity materialProductEntity = new MaterialProductDto(productDto, materialEntityList).toEntity();
        MaterialProductEntity resultMaterialProductEntity = materialProductRepository.save(materialProductEntity);

        if(resultMaterialProductEntity.getMpno() >= 1){ //등록 성공시
            return true;
        }

        return false;
    }

    //제품 수정 => 제품 관리 페이지에서 수행할 예정
    public int putProduct(ProductDto productDto){
        return 1;
    }

    //제품 삭제 => 제품 관리 페이지에서 수행할 예정
    public boolean deleteProduct(int prodId){
        return false;
    }
}
