import React, {useState, useEffect} from 'react';
import axios from 'axios'

/* -------------- mui -------------- */
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Pagination from '@mui/material/Pagination';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Container from '@mui/material/Container';

import {Checkbox} from '@mui/material';


export default function MaterialPrint(props){
    let [ pageInfo , setPageInfo ] = useState( { 'page' : 1 , 'keyword' : '' , 'matID' : 0 } )

    let[list, setList] = useState([]); //자재를 담을 배열 usestate
    const [checked, setChecked] = useState([]);
    let [totalPage , setTotalPage ] = useState(1);
    let [totalCount , setTotalCount ] = useState(1);

    let[existsMaterials, setExistMaterials] = useState([]);//이미 등록된 자재 리스트

    console.log(Array.isArray(props.existsM))
    console.log(existsMaterials)
     useEffect( ()=>{//컴포넌트 로딩시(열때, 시작시) 해당 쟈재 정보를 모두 가져온다.
        axios.get('/materials/materialList',  { params : pageInfo })
          .then( r => {
                console.log(r)
                setList(r.data.materialList)//가져온 자재 정보를 list에 담는다.
                setTotalPage(r.data.totalPage);
                setTotalCount(r.data.totalCount);
                setExistMaterials(props.existsM)
            } )
    }, [pageInfo] )

    //체크 박스 업데이트(기존 값을 가져와야해서 두가지의 경우를 체크해줘야함... 그리고 기존 값이 들어가는 걸로 갱신...)
    const checkboxEventHandler = (matID, existsMaterials) => {
      if (existsMaterials.some((item) => item.matID === matID)) {
        // 이미 존재하는 경우 삭제
        const updatedMaterials = existsMaterials.filter((item) => item.matID !== matID);
        setExistMaterials(updatedMaterials);

        setChecked(checked.filter((checking) => checking.matID !== setExistMaterials.matID));
      } else {
        // 존재하지 않는 경우 추가
        const updatedMaterials = [...existsMaterials, { matID }];
        setExistMaterials(updatedMaterials);
        setChecked([...checked, updatedMaterials.matID]);
      }
    };

    //자재 페이지 변경
    const selectPage = (event , value) => {
        console.log(value); //
        pageInfo.page = value;
        setPageInfo({...pageInfo});
    }

    //자재 검색
    const onSearch =()=>{
        pageInfo.keyword = document.querySelector(".keyword").value;
        pageInfo.page = 1
        document.querySelector(".keyword").value = '';
        setPageInfo({...pageInfo});
    }

    //수정된 자재목록 전송(부모에게)
    props.editM(checked);


return( <>
       <div>
            <TableContainer component={Paper}>
                      <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                          <TableRow>
                            <TableCell align="center" style={{ width:'5%' }}>등록번호</TableCell>
                            <TableCell align="center" style={{ width:'15%' }}>자재명</TableCell>
                            <TableCell align="center" style={{ width:'10%' }}>원가</TableCell>
                            <TableCell align="center" style={{ width:'10%' }}>단위</TableCell>
                            <TableCell align="center" style={{ width:'10%' }}>유통기한(Day)</TableCell>
                            <TableCell align="center" style={{ width:'10%' }}>생산자</TableCell>
                            <TableCell align="center" style={{ width:'15%' }}>구입일</TableCell>
                            <TableCell align="center" style={{ width:'5%' }}>코드</TableCell>
                            <TableCell align="center" style={{ width:'20%' }}>선택</TableCell>
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {list.map((e) => (
                            <TableRow>
                             <TableCell align="center" >{e.matID}</TableCell>
                             <TableCell align="center" >{e.mat_name}</TableCell>
                             <TableCell align="center" >{e.mat_price}</TableCell>
                             <TableCell align="center" >{e.mat_unit}</TableCell>
                             <TableCell align="center" >{e.mat_st_exp}</TableCell>
                             <TableCell align="center" >{e.companyDto.cname}</TableCell>
                             <TableCell align="center" >{e.mdate}</TableCell>
                             <TableCell align="center" >{e.mat_code}</TableCell>
                             <TableCell align="center"><Checkbox
                                onChange={() => checkboxEventHandler(e.matID, existsMaterials)}
                                checked={existsMaterials.some((item) => item.matID === e.matID)}/></TableCell>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>
            </div>
       </>);
}

{/*checked={existsMaterials.some((item) => item.matId === e.matID)}*/}