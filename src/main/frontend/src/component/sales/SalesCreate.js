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
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';

export default function SalesCreate() {

    const [ list , setList ] = useState([])     // 회사
    const [ listProduct , setListProduct ] = useState([])   // 물품

    // 회사 호출
    useEffect ( () => {
        axios.get('/sales/getcompany')
            .then( r => {
                console.log(r)
                setList(r.data)
            })
    }, [] )

    // 물품 호출
        useEffect ( () => {
            axios.get('/sales/getproduct')
                .then( r => {
                    console.log(r)
                    setListProduct(r.data)
                })
        }, [] )

    const salesCreate = () => {

        if ( company == 0 ){
            alert('회사를 선택해주세요.')
            return false;
        }
        if ( prodName == 0 ){
            alert('판매할 물품 이름을 선택해주세요.')
            return false;
        }

        let info = { // mno , order_status 추가적으로 필요
          orderCount: document.getElementById('orderCount').value,
          orderDate : document.getElementById('orderDate').value,
          salesPrice: document.getElementById('salesPrice').value,
          cno : company ,
          prodId : prodName
        }

        console.log(info)

        axios.post('/sales/salesCreate', info)
          .then(r => { console.log(r);
            if (r.data === true) {
              alert('등록성공')
              window.location.href = "/component/sales/SalesHeader"
            }
          })
          .catch(error => {
            console.error(error);
          });
      }

    const [company, setCompany] = useState(0);
        const handleChange = (event) => {
            console.log(event.target.value)
            setCompany(event.target.value);

        };

    const [ prodName, setProdName] = useState(0);
            const handleChange2 = (event) => {
                console.log(event.target.value)
                setProdName(event.target.value);

        };

      return (
                 <div style={{border: "2px solid #1a75ff" , borderRadius : '15px'}}>
                 <div style={{display : 'flex' , padding : '10px', margin : '10px'}}>
                        <Box sx={{ minWidth: 120 }}>
                                   <FormControl style={{ width : '100px' , margin : '20px 0px'}}>
                                     <InputLabel id="demo-simple-select-label">회사</InputLabel>
                                     <Select  value={ company } label="카테고리" onChange={ handleChange } >
                                         <MenuItem value={0}>회사</MenuItem>
                                         {
                                             list.map( (c) => {
                                                 return   <MenuItem value={c.cno}> { c.cname } </MenuItem>
                                             })
                                         }
                                     </Select>
                                   </FormControl>
                                 </Box>
                        <Box sx={{ minWidth: 120 }}>
                                   <FormControl style={{ width : '100px' , margin : '20px 0px'}}>
                                     <InputLabel id="demo-simple-select-label">물품</InputLabel>
                                     <Select  value={ prodName } label="카테고리" onChange={ handleChange2 } >
                                         <MenuItem value={0}>물품이름</MenuItem>
                                         {
                                             listProduct.map( (p) => {
                                                 return   <MenuItem value={p.prodId}>{ p.prodName }</MenuItem>
                                             })
                                         }
                                     </Select>
                                   </FormControl>
                                 </Box>
                         </div>
                 <div>
                            <TextField style={{padding : '10px', margin : '10px'}} className="orderDate" id="orderDate" label="판매날짜" variant="outlined" />
                           <TextField style={{padding : '10px', margin : '10px'}} className="orderCount" id="orderCount" label="판매개수" variant="outlined" />
                           <TextField style={{padding : '10px', margin : '10px'}} className="salesPrice" id="salesPrice" label="판매가격" variant="outlined" />

                     <Stack spacing={2} direction="row">
                         <Button style={{padding : '10px', margin : '10px 20px'}}variant="contained" type="button" onClick={salesCreate}>판매 등록</Button>
                     </Stack>
                  </div>
                 </div>

               );
}