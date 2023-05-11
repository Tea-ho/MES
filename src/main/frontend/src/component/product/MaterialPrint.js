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

     useEffect( ()=>{//컴포넌트 로딩시(열때, 시작시) 해당 쟈재 정보를 모두 가져온다.
        axios.get('/materials/materialList',  { params : pageInfo })
          .then( r => {
                console.log(r)
                setList(r.data.materialList)//가져온 자재 정보를 list에 담는다.
                setTotalPage(r.data.totalPage);
                setTotalCount(r.data.totalCount);
            } )
    }, [pageInfo] )

    //체크 박스 업데이트
    const checkboxEventHandler = (num) => {
       console.log(checked)
       if (checked.includes(num)) { //배열에 저장되어있는데 체크박스를 누른거면 취소니까 해당 배열에 그 번호를 삭제해준다
           setChecked(checked.filter((checked) => checked !== num));
       }else{
            setChecked([...checked, num]); //배열에 해당 클릭한 번호 저장
       }
    }

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
                             <TableCell align="center"><Checkbox onChange={() => checkboxEventHandler(e.matID)}/></TableCell>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>
            </div>
       </>);
}