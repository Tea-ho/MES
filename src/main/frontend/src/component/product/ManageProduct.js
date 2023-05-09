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

import Button from '@mui/material/Button';
import Pagination from '@mui/material/Pagination';

import {Container} from '@mui/material'
/* -------------- mui -------------- */

import EditProduct from './EditProduct'
export default function ManageProduct(props){
    let[pageInfo, setPageInfo] = useState({"page" : 1, "key" : '', "keyword" : ''}) //검색기능과 페이지네이션을 위해
    let[totalPage, setTotalPage] = useState(1); //총 페이지수
    let[totalCount, setTotalCount] = useState(0); //총 몇개의 제품

    let[productList, setProductList] = useState([]); //모든 제품을 담는

    let[putProduct, setPutProduct] = useState({}); //수정할 목록

    const getProduct = () => {
        axios.get("/product", {params : pageInfo})
            .then(r => {
                console.log(r.data);
                setProductList(r.data.productDtoList);
                setTotalCount(r.data.totalCount);
                setTotalPage(r.data.totalPage)
            })
    }

    useEffect(() => { //컴포넌트 재렌더링시 (시작시) 제품 정보 가져오기
       getProduct();
    }, [pageInfo])

    //검색
    const onSearch = () => {
        pageInfo.key = document.querySelector('.key').value; //검색할 대상
        pageInfo.keyword = document.querySelector('.keyword').value; //검색 값
        pageInfo.page = 1; //페이지 1
        console.log("key : " + pageInfo.key + " " + "keyword : " + pageInfo.keyword)

        setPageInfo({...pageInfo})
    }

    //페이지네이션 페이지 선택
    const selectPage = (e, value) => {
            console.log(value)
            pageInfo.page = value; //클릭된 페이지 번호를 가져와서 상태변수에 대입
            setPageInfo({...pageInfo}) //클릭된 페이지번호를 상태변수에 저장
    }

    //제품 수정 [제품 번호를 눌러야함...]
    const onUpdateHandler = (e) => {
       console.log(e)
       console.log(e.target.innerHTML)

       let uproduct = productList.find(f => f.prodId == e.target.innerHTML)
       setPutProduct({...uproduct})

       console.log(uproduct)
    }


    return(<>
         <Container>
            <div>현재페이지 : {pageInfo.page}  게시물 수 : {totalCount}</div>
             <TableContainer component={Paper}>
                  <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                      <TableRow>
                        <TableCell align ="center" style={{width:'10%'}}>제품번호</TableCell>
                        <TableCell align ="center" style={{width:'10%'}}>제품코드</TableCell>
                        <TableCell align ="center" style={{width:'20%'}}>등록날짜</TableCell>
                        <TableCell align ="center" style={{width:'35%'}}>제품명</TableCell>
                        <TableCell align ="center" style={{width:'20%'}}>제품가격</TableCell>
                        <TableCell align ="center" style={{width:'25%'}}>회사명</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {productList.map((row) => (
                          <TableRow
                              key={row.name}
                              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                              onClick ={onUpdateHandler}
                          >
                          <TableCell component ="th" align="center" scope="row">{row.prodId}</TableCell>
                          <TableCell component ="th" align="left">{row.prodCode}</TableCell>
                          <TableCell component ="th" align="center">{row.prodDate}</TableCell>
                          <TableCell component ="th" align="center">{row.prodName}</TableCell>
                          <TableCell component ="th" align="center">{row.prodPrice}</TableCell>
                          <TableCell component ="th" align="center">{row.companyEntity.cname}</TableCell>
                          </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
                <div class ="searchBox" style={{display : "flex", justifyContent : "center", margin : '40px 0px'}}>
                     <select className ="key">
                         <option value = "cname">회사명</option>
                         <option value = "prodName">제품명</option>
                     </select>
                     <input type = "text" className = "keyword"/>
                     <button type = "button" onClick={onSearch}>검색</button>
                 </div>
                <div style={{display : "flex", justifyContent : "center", margin : '40px 0px'}}>
                    <Pagination count={totalPage} page = {pageInfo.page} color="primary" onChange = {selectPage}/>
                </div>
                <div style={{display : 'flex' , justifyContent : 'center', marginTop:'30px'}}>
                    <EditProduct product={putProduct} callback={getProduct}/> {/*선택한 자재 PK를 제품 입력칸 부분에 전달*/}
                </div>
          </Container>
    </>)
}