import React,{ useEffect , useState , useRef } from 'react';
import {useParams} from 'react-router-dom'; // HTTP경로상의 매개변수 호출
import axios from 'axios';
/* ---------table mui -------- */
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Pagination from '@mui/material/Pagination';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import ButtonGroup from '@mui/material/ButtonGroup';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';

import SalesHeader from './SalesHeader'

export default function SalesView( props ){
    const [list, setList] = useState([]);

    const param2 = useParams();

    let [ pageInfo2 , setPageInfo2 ] = useState( { 'page' : 1 , 'keyword' : '' , 'order_id' : 0 } )
    let[totalPage2, setTotalPage2] = useState(1); //총 페이지수
    let[totalCount2, setTotalCount2] = useState(0); //총 판매 등록 개수

    // 판매 출력 페이지 렌더링
    useEffect(() => {
      axios.get('/sales/salesView', { params: pageInfo2 })
        .then(r => {
          console.log(r);
          setList(r.data.salesDtoList);
          setTotalPage2(r.data.totalPage2);
          setTotalCount2(r.data.totalCount2);
        });
    }, [pageInfo2]);

    // sales 검색
    const onSearch2 =()=>{
        pageInfo2.keyword = document.querySelector(".keyword2").value;
        pageInfo2.page = 1
        document.querySelector(".keyword").value = '';
        setPageInfo2({...pageInfo2});
    }

    // sales 페이지 선택
    const selectPage2 = (e , value) => {
        console.log(value); //
        pageInfo2.page = value;
        setPageInfo2({...pageInfo2});
    }

    // 판매 삭제
    const SalesDelete = (e) => {
        const order_id = e.target.value
        axios.delete('/sales/delete' , {params : { order_id : order_id}})
            .then ( r => {
                console.log(r);
                if( r.data == true ) {
                    alert('판매등록이 취소되었습니다.')
                    window.location.href = "/component/sales/SalesHeader"
                    }
            })
    }

    // 판매 수정
    const SalesUpdate = (e) => {
        console.log(e.target.value)
    }

    // 판매 확정
    const SalesResult = (e) => {
         console.log(e.target.value)
         const [order_id, al_app_no , prodId] = e.target.value.split(",");
         let info = {
                    al_app_no : al_app_no ,
                    order_id : order_id ,
                    prodId : prodId
         }
         axios.put('/sales/SalesStock' , info )
             .then ( r => {
                 console.log(r);
                 if( r.data == true ) {
                     alert('판매 최종 확정 처리되었습니다.')
                     window.location.href = "/component/sales/SalesHeader"
                     }
             })
    }


    return (

        <div>
                <TableContainer component={Paper}>
                          <Table sx={{ minWidth: 650 }} aria-label="simple table">
                            <TableHead>
                              <TableRow>
                                <TableCell align="center" style={{ width:'5%' }}>판매번호</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>판매날짜</TableCell>
                                <TableCell align="center" style={{ width:'15%' }}>물품번호</TableCell>
                                <TableCell align="center" style={{ width:'15%' }}>판매물품명</TableCell>
                                <TableCell align="center" style={{ width:'15%' }}>판매개수</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>판매가격</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>판매한 회사명</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>판매상태</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>비고</TableCell>

                              </TableRow>
                            </TableHead>


                            <TableBody>
                                {list.map((e) => (
                                  <TableRow>
                                    <TableCell align="center">{e.order_id}</TableCell>
                                    <TableCell align="center">{e.udate}</TableCell>
                                    <TableCell align="center">{e.prodId}</TableCell>
                                    <TableCell align="center">{e.prodName}</TableCell>
                                    <TableCell align="center">{e.orderCount}</TableCell>
                                    <TableCell align="center">{e.salesPrice}</TableCell>
                                    <TableCell align="center">{e.companyDto.cname}</TableCell>
                                    <TableCell align="center">{e.order_status == 0 ? '판매대기' : e.order_status == 1 ? '판매승인' : '판매확정' }</TableCell>

                                    <TableCell align="center">
                                      <ButtonGroup variant="contained" aria-label="outlined Secondary button group">
                                        {e.order_status === 0 ?
                                          <>
                                            <Button type="button" value={e.order_id} onClick={SalesDelete}>수정</Button>
                                            <Button type="button" value={e.order_id} onClick={SalesDelete}>삭제</Button>
                                          </>
                                          : e.order_status === 1 ?
                                          <Button type="button" value={`${e.order_id},${e.allowApprovalDto.al_app_no},${e.prodId}`} onClick={SalesResult}>판매확정</Button>
                                          : e.order_status === 2 ? <div> 판매완료 </div> : null
                                        }
                                      </ButtonGroup>
                                    </TableCell>

                                  </TableRow>
                                ))}
                            </TableBody>

                          </Table>
                        </TableContainer>

                        <div style={{display : 'flex' , justifyContent : 'center' }}>
                                    <Pagination count={totalPage2}  color="primary" onChange={selectPage2}/>
                        </div>

                        <div>
                            <input type="text" className="keyword2" />
                            <button type="button" onClick={onSearch2}> 검색 </button>
                         </div>
        </div>

    )

}