import React from 'react';
import Logo from './member/img/MESInfo.png';
import LoginSocket from './webSocket/LoginSocket'

export default function Header() {
  return (
    <div>
        <img src={Logo} width="100"/>
        <LoginSocket />
    </div>
  );
}