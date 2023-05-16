import React from 'react';
import Logo from './member/img/MESInfo.png';
import LoginSocket from './webSocket/LoginSocket'
import "./Header.css";

export default function Header() {
  return (
    <div style={{ display: 'flex', padding: '10px', margin: '10px', justifyContent: 'space-between' }}>
        <img src={Logo} width="100"/>
        <div  className='header' style={{ width: '40%', height: '120px'}}>
        <LoginSocket />
        </div>

    </div>
  );
}