import React,{ useState , useEffect } from 'react';
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

import SalesCreate from "./SalesCreate";
import SalesView from "./SalesView"
import SalesViewApp from "./SalesViewApp"

export default function SalesHeader() {
  return (
    <>
      <div>
        <h3> 판매 등록 </h3>
        <SalesCreate />
      </div>

      <div>
        <h3> 판매 현황1 (판매 승인 전) </h3>
        <SalesView />
      </div>

      <div>
        <h3> 판매 현황2 (판매 승인 후) </h3>
        <SalesViewApp />
      </div>
    </>
  );
}






