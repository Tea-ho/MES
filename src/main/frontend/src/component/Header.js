import React from 'react';

import LoginSocket from './webSocket/LoginSocket'
import SwipeableTemporaryDrawer from './SwipeableTemporaryDrawer'
import HeaderImg from '../component/member/img/Header.jpg';

export default function Header() {
  return (<>
        <div style={{ display: 'flex', justifyContent: 'space-between' , backgroundImage: `url(${HeaderImg})` , backgroundSize: 'cover' , width : '100%' , height : '150px'}}>



            <div style={{ display: 'flex' , width : '50%' }}>
                <SwipeableTemporaryDrawer />
            </div>



            <div style={{ width: '50%' , display: 'flex' , justifyContent: 'right'}}>
                <LoginSocket />
            </div>


        </div>
     </>
  );
}