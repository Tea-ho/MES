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

import javax.transaction.Transactional;
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
        System.out.println(pageDto);
        Pageable pageable = PageRequest.of(pageDto.getPage()-1, 5, Sort.by(Sort.Direction.DESC, "prod_id") );

        //페이징 처리를 위해 해당 키와 키워드 페이지 5개씩 지정
        Page<ProductEntity> pageEntity = productRepository.findBySearch(pageable, pageDto.getKey(), pageDto.getKeyword());
        System.out.println(pageEntity);
        List<ProductDto> productDto = new ArrayList<ProductDto>();
        
        //해당 pageEntity는 필요한 정보값(제품)을 가지고 있는 리스트이다.
        pageEntity.forEach((p) -> {
            productDto.add(p.toDto());
        });


        for(int i = 0; i < productDto.size(); i++){ //회사 정보 담아서 내보내기 위해
            productDto.get(i).setCompanyEntity(productRepository.findById(productDto.get(i).getProdId()).get().getCompanyEntity());
            System.out.println(productDto.get(i));
        }
        pageDto.setProductDtoList(productDto);
        pageDto.setTotalPage(pageEntity.getTotalPages());
        pageDto.setTotalCount(pageEntity.getTotalElements());

        return pageDto;
    }

    //제품 등록 => 제품 생산페이지에서 수행 할 예정
    public boolean postProduct(ProductDto productDto){

        List<MaterialEntity> materialEntityList = new ArrayList<>(); //자재 리스트(제품마다 가지고 있는)

        List<MaterialProductEntity> materialProductEntityList = new ArrayList<>(); //제품-자재 리스트

        for(int i = 0; i < productDto.getMaterialList().size(); i++){ //받은 자재 PK로 자재 찾아서 제품별 자재 리스트를 만든다
            materialEntityList.add(materialEntityRepository.findById(productDto.getMaterialList().get(i)).get());
        }

        System.out.println("자재 조회 : " + materialEntityList);

        ProductEntity productEntity = productRepository.save(productDto.toEntity());

        if(productEntity.getProdId() < 1){ //앞부분 등록 실패시 (제품 등록 실패시 - 자재 제외하고 온랴 제품테이블만)
            return false;
        }

        System.out.println("제품 등록 후 " + productEntity);

        MaterialProductEntity materialProductEntity = new MaterialProductEntity();
        materialProductEntity.setProductEntity(productEntity); //제품-재고 테이블에 제품 넣기
        materialProductEntity.setMaterialEntityList(materialEntityList); // 제품-재고 테이블에 재고 리스트 넣기

        //제품-재고 테이블에 필요한 정보를 set으로 다 넣었다면 save
        MaterialProductEntity resultMaterialProductEntity = materialProductRepository.save(materialProductEntity);

        System.out.println(resultMaterialProductEntity);

        for(int i = 0; i < materialEntityList.size(); i++){
            materialEntityList.get(i).setMaterialProductEntity(resultMaterialProductEntity);
            materialEntityRepository.save(materialEntityList.get(i));
            System.out.println(materialEntityList.get(i).toDto());
        }

        System.out.println(resultMaterialProductEntity.getMpno());

        if(resultMaterialProductEntity.getMpno() >= 1){ //등록 성공시
            System.out.println(resultMaterialProductEntity.toDto());
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
