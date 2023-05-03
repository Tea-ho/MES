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


export default function MaterialView(props) {

const [list, setList] = useState([]);

     useEffect( ()=>{
            axios.get('/materials/materialList')
              .then( r => {
                    console.log(r)
                    setList(r.data)
                } )

        }, [] )


    const MaterialUpdate=(e)=>{
        console.log(e.target.value)

    }

     const MaterialDelete=(e)=>{
            console.log(e.target.value)

        }

 return(<>

       <div>
      <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                  <TableHead>
                    <TableRow>
                      <TableCell align="center" style={{ width:'10%' }}>등록번호</TableCell>
                      <TableCell align="center" style={{ width:'15%' }}>자재명</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>원가</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>단위</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>유통기한(Day)</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>생산자</TableCell>
                      <TableCell align="center" style={{ width:'15%' }}>구입일</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>코드</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>수정/삭제</TableCell>
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
                       <TableCell align="center" >{e.companyEntity.cname}</TableCell>
                       <TableCell align="center" >{e.mdate}</TableCell>
                       <TableCell align="center" >{e.mat_code}</TableCell>
                       <TableCell align="center" >
                           <ButtonGroup variant="contained" aria-label="outlined Secondary button group">
                             <Button type="button" value={e.matID} onClick={MaterialUpdate}>수정</Button>
                             <Button type="button" value={e.matID} onClick={MaterialDelete}>삭제</Button>
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

} //<Pagination count={totalPage}  color="primary" onChange={selectPage}/>