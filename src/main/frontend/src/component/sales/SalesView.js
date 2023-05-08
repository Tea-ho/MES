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
export default function SalesView( props ){

    const [ list , setList ] = useState([]);

    useEffect ( () => {
        axios.get('/sales/salesView' , { params : { OrderId : 1 } })
        .then ( r => {
            console.log(r);
            setList(r.data);
        })
    } , [] )

    // 판매 수정
    const SalesUpdate=(e)=>{
        console.log(e.target.value)

    }
    // 판매 삭제
     const SalesDelete=(e)=>{
            console.log(e.target.value)

        }

    return(<>


       <div>
      <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                  <TableHead>
                    <TableRow>
                      <TableCell align="center" style={{ width:'5%' }}>판매개수</TableCell>
                      <TableCell align="center" style={{ width:'15%' }}>판매날짜</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>판매가격</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>판매물품명</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {list.map((e) => (
                      <TableRow>
                       <TableCell align="center" >{e.order_count}</TableCell>
                       <TableCell align="center" >{e.order_date}</TableCell>
                       <TableCell align="center" >{e.sales_price}</TableCell>
                       <TableCell align="center" >{e.prod_name}</TableCell>
                       <TableCell align="center" >
                           <ButtonGroup variant="contained" aria-label="outlined Secondary button group">
                             <Button type="button" value={e.OrderId} onClick={SalesUpdate}>수정</Button>
                             <Button type="button" value={e.OrderId} onClick={SalesDelete}>삭제</Button>
                           </ButtonGroup>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
              <div style={{display : 'flex' , justifyContent : 'center' }}>

              </div>

       </div>


    </>)

}