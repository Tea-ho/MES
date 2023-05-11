import React,{ useEffect , useState , useRef } from 'react';
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

import SalesHeader from './SalesHeader'

export default function SalesView( props ){
    const [list, setList] = useState([]);

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

                                <TableCell align="center" style={{ width:'10%' }}>판매상태(0:판매대기 , 1:판매완료)</TableCell>
                              </TableRow>
                            </TableHead>


                            <TableBody>
                                {list.map((e) => (
                                  <TableRow>
                                    <TableCell align="center">{e.order_id}</TableCell>
                                    <TableCell align="center">{e.orderDate}</TableCell>
                                    <TableCell align="center">{e.prodId}</TableCell>
                                    <TableCell align="center">{e.prodName}</TableCell>
                                    <TableCell align="center">{e.orderCount}</TableCell>
                                    <TableCell align="center">{e.salesPrice}</TableCell>

                                    <TableCell align="center">{e.companyDto.cname}</TableCell>

                                    <TableCell align="center">{e.order_status == 0 ? '판매대기' : '판매완료' }</TableCell>
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