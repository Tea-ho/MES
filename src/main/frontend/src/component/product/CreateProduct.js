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
import TextField from '@mui/material/TextField'; //텍스트 필드

import {Checkbox} from '@mui/material';
import InProduct from './InProduct'
/* -------------- mui -------------- */


export default function CreateProduct(props){
    let [ pageInfo , setPageInfo ] = useState( { 'page' : 1 , 'keyword' : '' , 'matID' : 0 } )

    let[list, setList] = useState([]); //자재를 담을 배열 usestate
    const [checked, setChecked] = useState([]);
    let [totalPage , setTotalPage ] = useState(1);
    let [totalCount , setTotalCount ] = useState(1);


     useEffect( ()=>{//컴포넌트 로딩시(열때, 시작시) 해당 쟈재 정보를 모두 가져온다.
        axios.get('/materials/materialList',  { params : pageInfo })
          .then( r => {
                setList(r.data.materialList)//가져온 자재 정보를 list에 담는다.
                setTotalPage(r.data.totalPage);
                setTotalCount(r.data.totalCount);
            } )
    }, [pageInfo] )

    //체크 박스 업데이트
    const checkboxEventHandler = (event, num) => {
        let domValue = '.matRate'+num;
        let rate = document.querySelector(domValue)

        let referencesValue = {
            matId : num,
            matRate : rate ? Number(rate.value)  : 0
        }

       console.log(referencesValue)
       //배열의 요소중 matId가 같은게 하나라도 있으면 삭제한다.
       if (checked.some((item) => item.matId === referencesValue.matId)) {
         setChecked(checked.filter((item) => item.matId !== referencesValue.matId));
       } else {
         setChecked([...checked, referencesValue]);
       }
    }

    //비율 설정 바꿀 때 checkbox 바꾸기
    const rateChangeHandler = (event, num) => {
        let domValue = '.matRate'+num;
        let rate = document.querySelector(domValue)

        let referencesValue = {
            matId : num,
            matRate : rate ? Number(rate.value)  : 0
        }

        //비율 설정할 때 checked되어있을 경우만 비율 설정(체크되어있지 않으면 추가X)
        setChecked(checked.map((item) => {
               if (item.matId === referencesValue.matId) {
                   return {
                       ...item,
                       matRate: referencesValue.matRate //비율값만 변경
                   };
               }
               return item;
        }));
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


    //제품 생산 전에 앞서 생산하려면 자재의 정보를 뿌려줘야한다.
    return( <>
       <div>
            <TableContainer component={Paper}>
                      <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                          <TableRow>
                            <TableCell align="center" style={{ width:'3%' }}>등록번호</TableCell>
                            <TableCell align="center" style={{ width:'10%' }}>자재명</TableCell>
                            <TableCell align="center" style={{ width:'10%' }}>원가</TableCell>
                            <TableCell align="center" style={{ width:'10%' }}>단위</TableCell>
                            <TableCell align="center" style={{ width:'15%' }}>유통기한(Day)</TableCell>
                            <TableCell align="center" style={{ width:'10%' }}>생산자</TableCell>
                            <TableCell align="center" style={{ width:'10%' }}>구입일</TableCell>
                            <TableCell align="center" style={{ width:'3%' }}>코드</TableCell>
                            <TableCell align="center" style={{ width:'20%' }}>선택</TableCell>
                            <TableCell align="center" style={{ width:'20%' }}>비율설정</TableCell>
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
                             <TableCell align="center"><Checkbox onChange={(event) => checkboxEventHandler(event, e.matID)}/></TableCell>
                             <TableCell align="center" ><input style={{padding : '7px', margin : '3px'}} className={'matRate'+e.matID} id={'matRate'+e.matID} placeholder="비율" onChange = {(event) => rateChangeHandler(event, e.matID)}/></TableCell>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>
                    <Container>
                        <div style={{display : 'flex' , justifyContent : 'center', margin : '10px 0px 10px 0px'}}>
                              <Pagination count={totalPage}  color="primary" onChange={selectPage}/>
                        </div>
                        <div style={{display : 'flex' , justifyContent : 'center' }}>
                               <input type="text" className="keyword" />
                               <button type="button" onClick={onSearch}> 검색 </button>
                        </div>
                    </Container>
                    <div style={{display : 'flex' , justifyContent : 'center', marginTop:'30px'}}>
                        <InProduct material={checked} tapHandler={props.handleTap}/> {/*선택한 자재 PK를 제품 입력칸 부분에 전달*/}
                    </div>
             </div>
    </>);
}