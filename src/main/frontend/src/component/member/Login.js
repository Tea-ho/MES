import React,{ useState , useEffect } from 'react';
import axios from 'axios';
import { Typography, Box } from '@mui/material';

export default function Login(props) {

    const [ username, setUsername ] = useState('');
    const [ password, setPassword ] = useState('');
    const [ loggedIn, setLoggedIn ] = useState(false);

    const handleUsernameChange = (event) => setUsername(event.target.value);
    const handlePasswordChange = (event) => setPassword(event.target.value);

    const handleLogin = () => {
        axios.get('/home/login',{ params: { username: username, password: password }})
            .then(response => {
                setLoggedIn(true);
                console.log('로그인 성공');
            })
            .catch(error => {
                console.error('로그인 실패', error);
            });
    };

    const handleLogout = () => {
        axios.get('/home/logout', { withCredentials: true })
            .then(response => {
                setLoggedIn(false);
                console.log('로그아웃 성공');
            })
            .catch(error => {
                console.error('로그아웃 실패:', error);
            });
        };


    return ( <>
        <div>
            {loggedIn
                ?
                <button onClick={handleLogout}>로그아웃</button>
                :
                <div>
                    <input type="text" value={username} onChange={handleUsernameChange} />
                    <input type="password" value={password} onChange={handlePasswordChange} />
                    <button onClick={handleLogin}>로그인</button>
                </div>
            }
        </div>
        <Box mt={5}>
            <Copyright />
        </Box>
    </>);
}

function Copyright(props) {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            teoenginer, {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}