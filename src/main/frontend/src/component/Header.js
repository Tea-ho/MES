import React from 'react';
import Logo from './member/img/MESInfo.png';
import LoginSocket from './webSocket/LoginSocket'
import SwipeableTemporaryDrawer from './SwipeableTemporaryDrawer'

export default function Header() {
  return (<>
        <div style={{ display: 'flex', padding: '10px', margin: '10px', justifyContent: 'space-between' }}>



            <div style={{ display: 'flex' , width : '50%' }}>
                <span>
                   <img src={Logo} width="150"/>
                </span>
                <SwipeableTemporaryDrawer />
            </div>



            <div style={{ width: '50%' , display: 'flex' , justifyContent: 'right'}}>
                <LoginSocket />
            </div>


        </div>
     </>
  );
}