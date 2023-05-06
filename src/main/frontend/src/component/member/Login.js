import React,{ useState , useEffect } from 'react';
import axios from 'axios';
import { Container, Grid, Typography, TextField, Button, Box } from '@mui/material';

export default function Login(props) {

    const [ mname, setMname ] = useState('');
    const [ password, setPassword ] = useState('');
    const [ loggedIn, setLoggedIn ] = useState(false);

    const handleUsernameChange = (event) => setMname(event.target.value);
    const handlePasswordChange = (event) => setPassword(event.target.value);

    const handleLogin = () => {
        let data = { mname: mname, mpassword: password };
        axios.post('/home/login', data)
            .then( (response) => {
                   console.log(response.data);
                if(response.data){
                    setLoggedIn(true);
                    window.location.href = '/component/member/AllowApproval';
                } else{
                    window.location.href = '/component/member/Login';
                }
            })
            .catch(error => {
                console.error('로그인 실패', error);
            });
    };

    const handleLogout = () => {
        axios.get('/home/logout')
            .then(response => {
                setLoggedIn(false);
                console.log('로그아웃 성공');
            })
            .catch(error => {
                console.error('로그아웃 실패:', error);
            });
        };


    return (
        <Container component="main" maxWidth="xs" style={{marginTop: "8%"}}>
        <Grid container spacing={2}>
            <Grid item xs={12}>
                <Typography variant="h5" component="h1">
                  로그인
                </Typography>
            </Grid>
        </Grid>
        <form noValidate onSubmit={handleLogin}>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <TextField
                        variant="outlined"
                        required
                        fullWidth
                        id="username"
                        label="아이디"
                        name="mname"
                        autoComplete="mname"
                        onChange={handleUsernameChange}
                    />
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        variant="outlined"
                        required
                        fullWidth
                        type="password"
                        id="password"
                        label="패스워드"
                        name="password"
                        autoComplete="current-password"
                        onChange={handlePasswordChange}
                    />
                </Grid>
                <Grid item xs={12}>
                    <Button type="submit" fullWidth variant="contained" color="primary">
                        로그인
                    </Button>
                </Grid>
            </Grid>
        </form>
        <Box mt={5} className="copyright">
            <Copyright />
        </Box>
    </Container>);
}

// 하단 회사명
function Copyright(props) {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            MES, {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}