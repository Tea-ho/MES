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
    let [ pageInfo , setPageInfo ] = useState( { 'page' : 1 , 'keyword' : '' , 'order_id' : 0 } )
    let[totalPage, setTotalPage] = useState(1); //총 페이지수
    let[totalCount, setTotalCount] = useState(0); //총 판매 등록 개수

    // page 렌더링
    useEffect(() => {
      axios.get('/sales/salesView', { params: pageInfo })
        .then(r => {
          console.log(r);
          setList(r.data.salesDtoList);
          setTotalPage(r.data.totalPage);
          setTotalCount(r.data.totalCount);
        });
    }, [pageInfo]);

    // 검색
    const onSearch =()=>{
        pageInfo.keyword = document.querySelector(".keyword").value;
        pageInfo.page = 1
        document.querySelector(".keyword").value = '';
        setPageInfo({...pageInfo});
    }

    const selectPage = (e , value) => {

        console.log(value); //
        pageInfo.page = value;
        setPageInfo({...pageInfo});
    }

    return (
    <div>
      <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                  <TableHead>
                    <TableRow>
                      <TableCell align="center" style={{ width:'5%' }}>판매번호</TableCell>
                      <TableCell align="center" style={{ width:'15%' }}>판매개수</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>판매날짜</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>판매상태(1:판매대기 , 2:판매완료)</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>판매가격</TableCell>
                    </TableRow>
                  </TableHead>


                  <TableBody>
                      {list.map((e) => (
                        <TableRow>
                          <TableCell align="center">{e.order_id}</TableCell>
                          <TableCell align="center">{e.orderCount}</TableCell>
                          <TableCell align="center">{e.orderDate}</TableCell>
                          <TableCell align="center">{e.order_status}</TableCell>
                          <TableCell align="center">{e.salesPrice}</TableCell>
                        </TableRow>
                      ))}
                  </TableBody>

                </Table>
              </TableContainer>

              <div style={{display : 'flex' , justifyContent : 'center' }}>
                          <Pagination count={totalPage}  color="primary" onChange={selectPage}/>
              </div>
              <div>

                  <input type="text" className="keyword" />
                  <button type="button" onClick={onSearch}> 검색 </button>
               </div>
        </div>
    )

}