import React,{ useState , useEffect } from 'react';
import axios from 'axios';

import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
 Panel, Pagination, Container, Button, Box, InputLabel, MenuItem,
 FormControl, Select, Stack, TextField } from '@mui/material';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';

export default function PerformanceForm(props) {

    // 1. 상태변수
    const [ rows, setRows ] = useState([]);
    const [rowSelectionModel, setRowSelectionModel] = React.useState([]);
    const[ type, setType ] = useState(props.type);

    // 2. fetchRows 메소드 생성
    // 생성이유: Controller랑 소통 창구 (type: 1 - 생산실적, 2 - 판매실적)
    const fetchRows = (type, setRows) => {
            console.log(type);
        if( type === 1 ){
            axios.get('/perform/prdocution', { params: { type: type } })
                .then((r) => {
                        console.log(r);
                    setRows(r.data);
                })
                .catch((error) => { //--- 에러 처리
                    console.log(error);
                });
        } else if( type === 2 ){
            let id = 0;
            axios.get('/perform/sales', { params: { type: type, id: id } })
                .then((r) => {
                        console.log(r);
                    setRows(r.data);
                })
                .catch((error) => { //--- 에러 처리
                    console.log(error);
                });
        }
    };

    // 3. type 변경될 때 렌더링 진행
    useEffect(() => {
      fetchRows(type, setRows);
    }, [type]);

    return(<>
    </>)
}