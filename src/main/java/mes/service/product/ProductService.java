package mes.service.product;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import mes.domain.Repository.product.MaterialProductRepository;
import mes.domain.Repository.product.ProductRepository;
import mes.domain.dto.material.MaterialDto;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


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
            productDto.get(i).setCompanyDto(productRepository.findById(productDto.get(i).getProdId()).get().getCompanyEntity().toDto());
            System.out.println("제품 확인!!!!!! : "+productDto);

            System.out.println("자재는 들어왔나... : "+ materialProductRepository.findByMaterial(productDto.get(i).getProdId()));

            //제품 PK로 자재를 찾기 위해 materialProduct를 찾는다
            List<MaterialProductEntity> materialProductEntity = materialProductRepository.findByMaterial(productDto.get(i).getProdId());

            List<MaterialDto> materialDtoList = new ArrayList<>(); //제품에 담을 자재 목록

            System.out.println(materialDtoList);
            for(int j = 0; j < materialProductEntity.size(); j++){
                materialDtoList.add(materialProductEntity.get(j).getMaterialEntity().toDto());
            }

            productDto.get(i).setMaterialDtoList(materialDtoList);

            System.out.println("제품 출력 materialList 장착 : " + productDto.get(i));
        }
        pageDto.setProductDtoList(productDto);
        pageDto.setTotalPage(pageEntity.getTotalPages());
        pageDto.setTotalCount(pageEntity.getTotalElements());
        System.out.println("모두 장착 : " + pageDto);
        return pageDto;
    }

    @Transactional
    //제품 등록 => 제품 생산페이지에서 수행 할 예정
    public boolean postProduct(ProductDto productDto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<MaterialEntity> materialEntityList = new ArrayList<>(); //자재 리스트(제품마다 가지고 있는)

        List<MaterialProductEntity> materialProductEntityList = new ArrayList<>(); //제품-자재 리스트

        try {

            for (HashMap<String, Integer> item : productDto.getReferencesValue()) {
                int matId = (int) item.get("matId");
                System.out.println("matID : " + matId);
                int matRate = (int) item.get("matRate");

                materialEntityList.add(materialEntityRepository.findById(matId).get());
            }

            System.out.println("자재 조회 : " + materialEntityList);

            Optional<CompanyEntity> companyEntity = companyRepository.findById(productDto.getCompanyDto().getCno());
            ProductEntity productEntity;

            if(companyEntity.isPresent()){
                productEntity = productRepository.save(productDto.toEntity());
                productEntity.setCompanyEntity(companyEntity.get());
                System.out.println(productEntity);
                if (productEntity.getProdId() < 1) { //앞부분 등록 실패시 (제품 등록 실패시 - 자재 제외하고 온랴 제품테이블만)
                    return false;
                }
                System.out.println("제품 등록 후 " + productEntity);
                List<MaterialProductEntity> resultMaterialProductEntity = new ArrayList<>();

                for (int i = 0; i < materialEntityList.size(); i++) {
                    MaterialProductEntity materialProductEntity = new MaterialProductEntity(materialEntityList.get(i), productEntity);
                    //제품-재고 테이블에 필요한 정보를 set으로 다 넣었다면 save
                    for (HashMap<String, Integer> item : productDto.getReferencesValue()) {
                        if (materialEntityList.get(i).getMatID() == (int) item.get("matId")){
                            materialProductEntity.setReferencesValue((int) item.get("matRate"));
                        }
                    }

                    resultMaterialProductEntity.add(materialProductRepository.save(materialProductEntity));
                    System.out.println(resultMaterialProductEntity);
                }

                for (int j = 0; j < resultMaterialProductEntity.size(); j++) {//새로 추가하는 mpno
                    materialProductEntityList.add(resultMaterialProductEntity.get(j));
                }

                for (int i = 0; i < materialEntityList.size(); i++) {
/*
            for(int j = 0; j < materialEntityList.get(i).getMaterialProductEntityList().size(); j++){//기존 mpno
                materialProductEntityList.add(materialEntityList.get(i).getMaterialProductEntityList().get(j));
            }*/

                    materialEntityList.get(i).setMaterialProductEntityList(materialProductEntityList);
                }

                if (resultMaterialProductEntity.get(resultMaterialProductEntity.size() - 1).getMpno() >= 1) { //등록 성공시
                    return true;
                }
            }
            return false;

        }catch (Exception e){
            System.err.println(e.getMessage());
            return false;
        }

    }

    @Transactional
    //제품 수정 => 제품 관리 페이지에서 수행할 예정
    public boolean putProduct(ProductDto productDto){
        System.out.println(productDto);
        Optional<ProductEntity> putProductEntity = productRepository.findById(productDto.getProdId());

        if(putProductEntity.isPresent()){
            ProductEntity productEntity = putProductEntity.get();
            productEntity.setCompanyEntity(productDto.getCompanyDto().toEntity());
            productEntity.setProdPrice(productDto.getProdPrice());
            productEntity.setProdName(productDto.getProdName());
            return true;
        }

        return false;
    }

    //제품 삭제 => 제품 관리 페이지에서 수행할 예정
    public boolean deleteProduct(int prodId){
        return false;
    }
}
