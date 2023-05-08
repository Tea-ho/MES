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

    const SalesCreate = () => {

        let info = {
          order_count: document.getElementById('order_count').value,
          order_date: document.getElementById('order_date').value,
          sales_price: document.getElementById('sales_price').value,
          prod_name: document.getElementById('prod_name').value,
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

      return (
        <div>
          <div>
            <TextField style={{ padding: '10px', margin: '10px' }} className="order_count" id="order_count" label="판매개수" variant="outlined" />
            <TextField style={{ padding: '10px', margin: '10px' }} className="order_date" id="order_date" label="판매날짜" variant="outlined" />
            <TextField style={{ padding: '10px', margin: '10px' }} className="sales_price" id="sales_price" label="판매가격" variant="outlined" />
            <TextField style={{ padding: '10px', margin: '10px' }} className="prod_name" id="prod_name" label="물품이름" variant="outlined" />
          </div>

          <Stack spacing={2} direction="row">
            <Button style={{ padding: '10px', margin: '10px 20px' }} variant="contained" type="button" onClick={SalesCreate}>판매 등록</Button>
          </Stack>
        </div>
      );
    }