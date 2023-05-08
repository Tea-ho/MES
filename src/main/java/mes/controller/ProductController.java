package mes.controller;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.product.PageDto;
import mes.domain.dto.product.ProductDto;
import mes.service.product.ProductPlanService;
import mes.service.product.ProductProcessService;
import mes.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService; //간단한 제품 CRUD 기능 수행
    
    @Autowired
    private ProductPlanService productPlanService; // 제품 지시관련 기능 수행
    
    @Autowired
    private ProductProcessService productProcessService; //제품 공정 관련 기능 수행

    
    //전체 제품 출력 => 제품 지시, 제품 관리에서 출력 => 제품쪽에서는 재고 출력
    @GetMapping("")
    public PageDto getProductList(PageDto pageDto){
        System.out.println(pageDto.toString());
        return productService.getProductList(pageDto);
    }
    
    //제품 등록
    @PostMapping("")
    public boolean postProduct(@RequestBody ProductDto productDto){
        System.out.println(productDto.toString());
        return productService.postProduct(productDto);
    }

    //재품 수정
    @PutMapping("")
    public int putProduct(@RequestBody ProductDto productDto){
        System.out.println(productDto.toString());
        return 1;
    }

    //제품 삭제
    @DeleteMapping("")
    public boolean deleteProduct(@RequestParam int prodId){
        return false;
    }
}
