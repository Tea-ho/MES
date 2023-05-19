import * as React from 'react';
import Box from '@mui/material/Box';
import SwipeableDrawer from '@mui/material/SwipeableDrawer';
import Button from '@mui/material/Button';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import WarehouseIcon from '@mui/icons-material/Warehouse';
import Link from '@mui/material/Link';
import ReorderIcon from '@mui/icons-material/Reorder';
import HouseIcon from '@mui/icons-material/House';
import PrecisionManufacturingIcon from '@mui/icons-material/PrecisionManufacturing';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import FactCheckIcon from '@mui/icons-material/FactCheck';
import PercentIcon from '@mui/icons-material/Percent';
import Logo from './member/img/MESInfo.png';


export default function SwipeableTemporaryDrawer() {
  const [state, setState] = React.useState({

    left: false

  });

  const toggleDrawer = (anchor, open) => (event) => {
    if (
      event &&
      event.type === 'keydown' &&
      (event.key === 'Tab' || event.key === 'Shift')
    ) {
      return;
    }

    setState({ ...state, [anchor]: open });
  };

const list = (anchor: Anchor) => (
    <Box
      sx={{ width: 175  }}
      role="presentation"
      onClick={toggleDrawer(anchor, false)}
      onKeyDown={toggleDrawer(anchor, false)}
    >
      <nav aria-label="main mailbox folders">
                                <List>
                                  <ListItem disablePadding>
                                    <Link style={{ textDecoration: "none" ,  color : 'black' }}
                                            href="/component/member/Login">
                                    <ListItemButton>
                                         <ListItemIcon>
                                        <HouseIcon />
                                      </ListItemIcon>
                                      <ListItemText primary="HOME" />
                                     </ListItemButton>
                                     </Link>
                                  </ListItem>
                                  <ListItem disablePadding>
                                  <Link style={{ textDecoration: "none" ,  color : 'black' }}
                                        href="/component/material/Material">
                                   <ListItemButton>
                                   <ListItemIcon>
                                   <WarehouseIcon />
                                   </ListItemIcon>
                                   <ListItemText primary="자재" />
                                   </ListItemButton>
                                   </Link>
                                   </ListItem>
                                     <ListItem disablePadding>
                                    <Link style={{ textDecoration: "none" ,  color : 'black'}}
                                            href="/component/product/productTab">
                                    <ListItemButton>
                                         <ListItemIcon>
                                        <PrecisionManufacturingIcon />
                                      </ListItemIcon>
                                      <ListItemText primary="제품" />
                                     </ListItemButton>
                                     </Link>
                                     </ListItem>
                                     <ListItem disablePadding>
                                    <Link style={{ textDecoration: "none" ,  color : 'black'}}
                                            href="/component/sales/SalesHeader">
                                    <ListItemButton>
                                         <ListItemIcon>
                                        <AttachMoneyIcon />
                                      </ListItemIcon>
                                      <ListItemText primary="판매" />
                                     </ListItemButton>
                                     </Link>
                                  </ListItem>
                                  <ListItem disablePadding>
                                     <Link style={{ textDecoration: "none" ,  color : 'black'}}
                                            href="/component/member/AllowApproval">
                                     <ListItemButton>
                                          <ListItemIcon>
                                         <FactCheckIcon />
                                       </ListItemIcon>
                                       <ListItemText primary="승인" />
                                      </ListItemButton>
                                      </Link>
                                   </ListItem>
                                   <ListItem disablePadding>
                                   <Link style={{ textDecoration: "none" ,  color : 'black'}}
                                        href="/component/performance/Performance">
                                   <ListItemButton>
                                        <ListItemIcon>
                                       <PercentIcon />
                                     </ListItemIcon>
                                     <ListItemText primary="실적" />
                                    </ListItemButton>
                                    </Link>
                                 </ListItem>
                                </List>
                              </nav>
    </Box>
);


  return (
    <div>
      {['left'].map((anchor) => (
        <React.Fragment key={anchor}>
          <Button onClick={toggleDrawer(anchor, true)}><img src={Logo} width="150"/></Button>
          <SwipeableDrawer
            anchor={anchor}
            open={state[anchor]}
            onClose={toggleDrawer(anchor, false)}
            onOpen={toggleDrawer(anchor, true)}
          >
            {list(anchor)}
          </SwipeableDrawer>
        </React.Fragment>
      ))}
    </div>
  );
}