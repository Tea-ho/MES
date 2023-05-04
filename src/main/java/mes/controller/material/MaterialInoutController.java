package mes.controller.material;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.material.MaterialInOutDto;
import mes.service.Material.MaterialInoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/materialInout")
public class MaterialInoutController {

    @Autowired
    private MaterialInoutService materialInoutService;

    @PostMapping("/materialIn")
    public boolean materialIn(@RequestBody MaterialInOutDto dto){
        System.out.println("materialIn : " + dto);


        return materialInoutService.materialIn(dto);
    }

    @GetMapping("/MaterialInOutList")
    public List<MaterialInOutDto> MaterialInOutList(@RequestParam int matID){


        return materialInoutService.MaterialInOutList(matID);
    }

}
