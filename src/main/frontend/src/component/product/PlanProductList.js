import React,{useState, useEffect} from 'react';
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


export default function PlanProductList(props) {
   // 1. 상태변수 선언[ 결재 리스트 관리 Controller get으로 받아서 초기화 예정 / DataGrid 적용 ]
   const [ rows, setRows ] = useState([]);
   const [rowSelectionModel, setRowSelectionModel] = React.useState([]);

    useEffect(() => {
        getPlanProduct();
    }, [])

    const getPlanProduct = () => { //생산 지시 목록 가져오기
        axios.get('/planProduct')
            .then((r) => {
                console.log(r.data)
                setRows(r.data);
            })
    }


    // planProductDto를 담는 공간
     let columns = [
                { field: 'productDto.prodId', headerName: '제품번호', width: 70,},
                { field: 'productDto.prodName', headerName : '제품명', width : 100},
                { field: 'productDto.prodCode', headerName : '제품코드', width : 70},
                { field: 'productDto.prodPrice', headerName : '제품가격', width : 100},
                { field: 'productDto.companyDto.cname', headerName : '제조사' , width : 100 },
                { field: 'prodPlanDate', headerName: '요청일자', width: 80 },
                { field: 'allowApprovalEntity.al_app_whether', headerName: '승인여부', width: 70,
                  valueGetter: (params) => params.row.allowApprovalEntity.al_app_whether ? '승인완료' : '승인대기'
                },
                { field: 'allowApprovalEntity.memberEntity.mname', headerName: '요청자', width: 100},

            ]


    return(<>
        <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
            <button sx={{ padding: '10px', margin: '10px 20px' }} variant="contained"
                type ="button"
                onClick={putPlanProduct}
                disabled={ rowSelectionModel.length === 0 ? true : false }
            >
                수정
            </button>

            <button sx={{ padding: '10px', margin: '10px 20px' }} variant="contained"
                type ="button"
                onClick={deletePlanProduct}
                disabled={ rowSelectionModel.length === 0 ? true : false }
            >
                삭제
            </button>
        </Box>
        <div style={{ height: 400, width: '100%' }}>
            <DataGrid
                rows={rows}
                columns={columns}
                getRowId={type === 1 ? ((row) => row.prodPlanNo) : ((row) => null)}
                initialState={{
                    pagination: {
                        paginationModel: { page: 0, pageSize: 5 },
                    },
                }}
            pageSizeOptions={[5, 10]}
            isRowSelectable={(params)=>params.row.allowApprovalEntity.al_app_whether==true}
            checkboxSelection
            onRowSelectionModelChange={(newRowSelectionModel) => {
                setRowSelectionModel(newRowSelectionModel);
            }}
            />
        </div>
    </>);
}