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
/* ---------------------------*/
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
export default function MaterialCreate() {

    const MaterialCreate=()=>{

    let info={
        MatName : document.querySelector('.MatName').value,
        MatUnit : document.querySelector('.MatUnit').value,
        MatStExp : document.querySelector('.MatStExp').value,
        MatPrice : document.querySelector('.MatPrice').value
    }
    console.log(info)

    axios.post('/', info)
                .then( r => { console.log(r);


                } )


     }





  return (

    <div>
      자재이름 : <input type="text" className="MatName" />
      자재 단위 : <input type="text" className="MatUnit"/>
      유통기한 : <input type="text" className="MatStExp" />
      단가 : <input type="text" className="MatPrice" />
      <button type="button" onClick={MaterialCreate}> 자재생성</button>
    </div>

  );
}