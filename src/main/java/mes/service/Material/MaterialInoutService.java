package mes.service.Material;

import lombok.extern.slf4j.Slf4j;
import mes.controller.member.MemberController;
import mes.domain.dto.material.InOutPageDto;
import mes.domain.dto.material.MaterialInOutDto;
import mes.domain.dto.member.AllowApprovalDto;
import mes.domain.dto.member.MemberDto;
import mes.domain.entity.material.MaterialEntity;
import mes.domain.entity.material.MaterialEntityRepository;
import mes.domain.entity.material.MaterialInOutEntity;
import mes.domain.entity.material.MaterialInOutEntityRepository;
import mes.domain.entity.member.AllowApprovalEntity;
import mes.domain.entity.member.AllowApprovalRepository;
import mes.domain.entity.member.MemberEntity;
import mes.domain.entity.member.MemberRepository;
import mes.service.member.MemberSerivce;
import mes.webSocket.ChattingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.TextMessage;


import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MaterialInoutService {

    @Autowired
    private MaterialInOutEntityRepository materialInOutEntityRepository;

    @Autowired
    private MaterialEntityRepository materialEntityRepository;

    @Autowired
    private AllowApprovalRepository allowApprovalRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChattingHandler chattingHandler;

    // 입고내역
    @Transactional
    public boolean materialIn(MaterialInOutDto dto){
        if(dto.getMemberdto()==null){return false;}
        MemberEntity member = memberRepository.findByMnameAndMpassword(dto.getMemberdto().getMname() , dto.getMemberdto().getMpassword());

        MaterialInOutEntity entity = new MaterialInOutEntity();
        entity.setMemberEntity(dto.getMemberdto().toEntity());
        entity.setMat_in_type(dto.getMat_in_type());

        // 선택된 자재
        Optional<MaterialEntity> optionalMaterial = materialEntityRepository.findById(dto.getMatID());
        MaterialEntity materialEntity = optionalMaterial.get();
        // 승인정보
        AllowApprovalEntity approvalEntity = allowApprovalRepository.save(new AllowApprovalDto().toInEntity());

        // 자재와 데이터 넣기
        entity.setMaterialEntity(materialEntity);
        entity.setAllowApprovalEntity(approvalEntity);
        entity.setMemberEntity(member);
        entity.setMat_in_code(0); // 기본값


        // 세이브
        MaterialInOutEntity result = materialInOutEntityRepository.save(entity);


        // 소캣 메시지 전송
        // 자재 - 10 : 결제 대기중인 안건 / 11 : 결제 완료
        // 제품 - 20 : 결제 대기중인 안건 / 21 : 결제 완료
        // 판매 - 30 : 결제 대기중인 안건 / 31 : 결제 완료
        try{
            chattingHandler.handleMessage(null , new TextMessage("10"));

        }catch (Exception e){
            System.out.println(e);
        }



        if( result.getMat_in_outid() >= 1 ){ return true; }  // 2. 만약에 생성된 엔티티의 pk가 1보다 크면 save 성공
        return false;

    }

    // 내역 리스트 출력
    @Transactional
    public InOutPageDto MaterialInOutList(InOutPageDto dto){

       List<MaterialInOutDto> list = new ArrayList<>();
        Pageable pageable = PageRequest.of(dto.getPage()-1 , 5 , Sort.by(Sort.Direction.DESC , "udate"));

        Page<MaterialInOutEntity> entityPage = materialInOutEntityRepository.findByMatid(dto.getMatID() , pageable);
        entityPage.forEach((e)->{
            list.add(e.toDto());
        });

        dto.setMaterialInOutDtoList(list);
        dto.setTotalPage(entityPage.getTotalPages());
        dto.setTotalCount(entityPage.getTotalElements());

        return dto;
    }

    // 최종 완료 이후 재고증가 처리
    @Transactional
    public boolean MaterialInStock(MaterialInOutDto dto){

        // 해당 자재의 가장 마지막에 처리된(update) 레코드 가져오기
        Optional<MaterialInOutEntity> lastInOut = materialInOutEntityRepository.findByUdate(dto.getMatID());

        // 최종 확인 요청한 레코드 가져오기
        MaterialInOutEntity AllowIN = materialInOutEntityRepository.findByAlid(dto.getAl_app_no());

        log.info("MaterialInOut :" + lastInOut);
        log.info("AllowIN : " + AllowIN);
        int stock=0;

        if(!lastInOut.isPresent()){ // 처음이면 stock = 0
            stock=0;
        }
        else { stock = lastInOut.get().getMat_st_stock();} // 처음이 아니면 해당 레코드의 재고를 가져오기

        // 마지막에 처리된 레코드의 stock과 최종 확인한 레코드의 type값을 더해서 stock에 저장
        AllowIN.setMat_st_stock(stock+AllowIN.getMat_in_type());
        AllowIN.setMat_in_code(1); // 최종완료 확인처리
        materialInOutEntityRepository.save(AllowIN); // 세이브

        return true;
    }

    // 등록취소
    @Transactional
    public boolean MaterialDelete(int mat_in_outid){

        Optional<MaterialInOutEntity> entity = materialInOutEntityRepository.findById(mat_in_outid);
        if(entity.isPresent()) {
            MaterialInOutEntity materialInOutEntity = entity.get();
            allowApprovalRepository.delete(materialInOutEntity.getAllowApprovalEntity());
            materialInOutEntityRepository.delete(materialInOutEntity);
            return true;
        }


        return false;
    }

    @Transactional
    public boolean materialOut(MaterialInOutDto dto){
        System.out.println("자재 출고 : " + dto);

        System.out.println("로그인한 멤버 : " + dto.getMemberdto());

        /*//로그인한 사람의 정보 확인
        MemberEntity member = memberRepository.findByMnameAndMpassword(dto.getMemberdto().getMname() , dto.getMemberdto().getMpassword());
*/
        // materialInOutEntity에 materialEntity 설정

        MaterialInOutEntity entity = new MaterialInOutEntity();;
        entity.setMaterialEntity(dto.getMaterialDto().toOutEntity());
        entity.setMemberEntity(dto.getMemberdto().toEntity());
        entity.setMat_in_type(dto.getMat_in_type());
        entity.setMat_st_stock(dto.getMat_st_stock());
        entity.setMat_in_code(dto.getMat_in_code());

        System.out.println("자재 중간 점검 : " + entity);

        // 승인정보
        AllowApprovalEntity approvalEntity = allowApprovalRepository.save(dto.getAllowApprovalDto().toOutEntity());

        // 자재와 데이터 넣기
        entity.setAllowApprovalEntity(approvalEntity);

        // 세이브
        MaterialInOutEntity result = materialInOutEntityRepository.save(entity);

        if( result.getMat_in_outid() >= 1 ){ return true; }  // 2. 만약에 생성된 엔티티의 pk가 1보다 크면 save 성공
        return false;

    }


}
