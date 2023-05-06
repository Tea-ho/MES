import React,{ useState , useEffect } from 'react';
import axios from 'axios';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
 Panel, Pagination, Container, Button, Box, InputLabel, MenuItem,
 FormControl, Select, Stack, TextField } from '@mui/material';

export default function AllowMaterial() {

    // 1. 상태변수 선언[ 결재 리스트 관리 ]
    const [ list, setList ] = useState([])

    // 2. 데이터 가져오기 + 렌더링
    useEffect(() => {
        axios.get('/allowApproval', { params:{ type: 1} })
            .then( r => {
                console.log(r);
                setList(r.data);
            } )
    }, [])

    // 3. 승인 처리
    const approveHandler = (e) => {
        console.log(e.target.value);
    }
    // 4. 반려 처리
    const rejectHandler = (e) => {
        console.log(e.target.value);
    }

    // 5. 페이지네이션
    return(
        <div>
            <h3>자제 승인</h3>
            <div>
                <TableContainer>
                    <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell align="center" style={{ width:'5%' }}>번호</TableCell>
                                <TableCell align="center" style={{ width:'35%' }}>요청내용</TableCell>
                                <TableCell align="center" style={{ width:'20%' }}>요청일자</TableCell>
                                <TableCell align="center" style={{ width:'20%' }}>승인여부</TableCell>
                                <TableCell align="center" style={{ width:'20%' }}>비고</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {list.map((e) => (
                                <TableRow>
                                    <TableCell align="center" >0</TableCell>
                                    <TableCell align="center" >자재입고</TableCell>
                                    <TableCell align="center" >2022.04.11</TableCell>
                                    <TableCell align="center" >승인대기</TableCell>
                                    <TableCell align="center" >
                                        <Button variant="contained" color="primary" onClick={approveHandler}>승인</Button>
                                        <Button variant="contained" color="primary" onClick={rejectHandler}>반려</Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
        </div>
    )
}