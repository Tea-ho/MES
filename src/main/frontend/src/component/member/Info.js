import React,{ useState , useEffect } from 'react';
import axios from 'axios';
import { Container, Grid, Typography, TextField, Button, Box } from '@mui/material';

import Login from './Login';

export default function Info(props) {

    const { handleLogout } = props;
    const logoutEvnentHandler = ()=>{
        console.log("logout click");
        handleLogout();
    }
    return(
        <Container>
            <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
            <Button sx={{ padding: '10px', margin: '10px 20px' }} variant="contained" type="button" onClick={logoutEvnentHandler}>
                로그아웃
            </Button>
            </Box>
            <img src="./img/MESInfo.png" alt="MES Info" />
        </Container>
    )
}