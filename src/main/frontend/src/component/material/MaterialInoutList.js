import React,{ useEffect , useState , useRef } from 'react';
import axios from 'axios';
import {useParams} from 'react-router-dom'; // HTTP경로상의 매개변수 호출
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
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';

export default function MaterialInoutList(props) {


    const params = useParams(); // 매개변수가 객체형태로 들어옴.

    console.log(params.matID);

    const [list, setList] = useState([]);
    const [inOutList , setInOutList] = useState([]);

         useEffect( ()=>{
                axios.get('/materials/materialList' , { params : {matID : params.matID} })
                  .then( r => {
                        console.log(r)
                        setList(r.data)
                        MaterialInOut();
                    } )



            }, [] )


    const MaterialIn = () =>{
        let info = {
            mat_in_type : document.getElementById('mat_in_type').value,
            matID : params.matID
        }

        axios.post('/materialInout/materialIn', info)
                        .then( r => { console.log(r);
                        if(r.data == true){
                        alert('등록성공 결제대기합니다.');
                        window.location.href = `/component/material/MaterialInoutList/${params.matID}`;
                        }
                        } )
        }


    const MaterialInOut = () =>{
            axios.get('/materialInout/MaterialInOutList' , { params : {matID : params.matID} })
                     .then( r => {
                     console.log(r)
                     setInOutList(r.data);

            } )


    }




    return (<>

        <div>
        <h3>자제 상세내역</h3>
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
                                  </TableRow>
                                ))}
                              </TableBody>
                            </Table>
                          </TableContainer>

            </div>

        <div>
         <h3>자재 현황</h3>
                        <TableContainer component={Paper}>
                            <Table sx={{ minWidth: 650 }} aria-label="simple table">
                              <TableHead>
                                <TableRow>
                                  <TableCell align="center" style={{ width:'5%' }}>번호</TableCell>
                                  <TableCell align="center" style={{ width:'10%' }}>자재명</TableCell>
                                  <TableCell align="center" style={{ width:'10%' }}>원가</TableCell>
                                  <TableCell align="center" style={{ width:'10%' }}>단위</TableCell>
                                  <TableCell align="center" style={{ width:'10%' }}>수량</TableCell>
                                  <TableCell align="center" style={{ width:'10%' }}>재고</TableCell>
                                  <TableCell align="center" style={{ width:'10%' }}>결제 상태</TableCell>
                                  <TableCell align="center" style={{ width:'10%' }}>결제일</TableCell>
                                  <TableCell align="center" style={{ width:'5%' }}>승인자</TableCell>

                                </TableRow>
                              </TableHead>
                              <TableBody>
                                {inOutList.map((e) => (
                                  <TableRow>
                                   <TableCell align="center" >{e.mat_in_outid}</TableCell>
                                   <TableCell align="center" >{e.materialEntity.mat_name}</TableCell>
                                   <TableCell align="center" >{e.materialEntity.mat_price}</TableCell>
                                   <TableCell align="center" >{e.materialEntity.mat_unit}</TableCell>
                                   <TableCell align="center" >{e.mat_in_type}</TableCell>
                                   <TableCell align="center" >{e.mat_st_stock}</TableCell>
                                   <TableCell align="center" >{e.allowApprovalEntity.al_app_whether == false ? "결제대기중" : "결제완료"}</TableCell>
                                   <TableCell align="center" >{e.allowApprovalEntity.al_app_date == null ? "" : e.allowApprovalEntity.al_app_date}</TableCell>
                                   <TableCell align="center" >{e.allowApprovalEntity.memberEntity == null ? "" : "결제승인인원"}</TableCell>
                                  </TableRow>
                                ))}
                              </TableBody>
                            </Table>
                          </TableContainer>
        </div>


        <div>


        <h3> 자재 입고 </h3>
        <TextField style={{padding : '10px', margin : '10px'}} className="mat_in_type" id="mat_in_type" label="수량" variant="outlined" />
        <Stack spacing={2} direction="row">
            <Button style={{padding : '10px', margin : '10px 20px'}}variant="contained" type="button" onClick={MaterialIn}>입고</Button>
        </Stack>

        </div>




        </div>

    </>)

}